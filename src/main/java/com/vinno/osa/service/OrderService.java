package com.vinno.osa.service;
import com.vinno.osa.entity.*;
import com.vinno.osa.repository.CartRepository;
import com.vinno.osa.repository.CouponRepository;
import com.vinno.osa.repository.OrderRepository;
import com.vinno.osa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private  OrderRepository orderRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CartRepository cartRepository;

    public Orders placeOrder() {
        String userEmail = getAuthenticatedUserEmail();

        // Find the user's cart
        Cart cart = cartRepository.findByUserId(userEmail)
                .orElseThrow(() -> new RuntimeException("Cart not found for user " + userEmail));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Loop through cart items and add to the order
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found for ID " + cartItem.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
            totalPrice = totalPrice.add(orderItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }

        String estimatedDeliveryDate = LocalDate.now().plusDays(2).toString();

        Orders order = new Orders();
       order.setUserId(cart.getUserId());  // Use the user's email from JWT token
        order.setEmail(userEmail);
        order.setItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(totalPrice.doubleValue());
        order.setDeliveryDate(estimatedDeliveryDate);
        orderRepository.save(order);


      //  cart.getCartItems().clear();   // Clear the user's cart after placing the order
        cartRepository.save(cart);

        order.setStatus(OrderStatus.PROCESSING);
        String emailBody = generateOrderConfirmationBody(order);
        emailService.sendEmail(userEmail, "Order Confirmation", emailBody);

        return order;
    }

    private String getAuthenticatedUserEmail() {
        // Assuming you have a method to extract the email from the JWT token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();  // The email is stored as the principal
        }
        throw new RuntimeException("User is not authenticated");
    }
    public Orders getOrderById(String orderId) {
        return orderRepository.findById(orderId)        // Find the order by its ID in the repository
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }


   // Get orders by user email (userId will be the email in this case)
   public List<Orders> getOrdersByUserEmail(String userEmail) {
       return orderRepository.findByEmail(userEmail);            // Fetch orders based on the user's email
   }

    public BigDecimal calculateTotalPrice(List<OrderItem> items) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : items) {
            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }
        return totalPrice;
    }

    public Orders updateOrderStatus(String orderId, String newStatus) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        try {
            OrderStatus status = OrderStatus.valueOf(newStatus.toUpperCase());
            order.setStatus(status);  // Update the status
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + newStatus);
        }
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);  // This will update the order,Save the updated order back to the repository
    }

    public Orders cancelOrder(String orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CANCELLED); // Update status to "Canceled"
        return orderRepository.save(order); // Return the updated order
    }


    public Orders requestReturn(String orderId, String returnReason) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // Check if the return has already been processed
        if (order.isReturnProcessed()) {
            throw new RuntimeException("Return already processed for this order");
        }
        order.setUserId(order.getUserId());
        order.setReturnProcessed(true);          // Update return status and reason
        order.setReturnReason(returnReason);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return order;
    }


    // Method to approve the return request
    public Orders approveReturn(String orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getReturnStatus().equals("Requested")) {
            throw new RuntimeException("Return not requested for this order");
        }
        order.setReturnStatus("Approved");  // Mark return as approved
        BigDecimal refundAmount = calculateRefundAmount(order);  // Calculate the refund amount if it's a partial return
        order.setTotalPrice(refundAmount.doubleValue());
        return orderRepository.save(order);
    }

    public Orders processPartialReturn(String orderId, List<OrderItem> itemsToReturn) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getReturnStatus().equals("Requested")) {
            throw new RuntimeException("Return not requested for this order");
        }
        List<OrderItem> remainingItems = order.getItems().stream()         // Remove the returned items from the order
                .filter(item -> !itemsToReturn.contains(item))
                .collect(Collectors.toList());
        order.setItems(remainingItems);
        BigDecimal refundAmount = calculatePartialRefund(order, itemsToReturn);   // Calculate the refund for the returned items
        order.setTotalPrice(refundAmount.doubleValue());
        order.setReturnStatus("Approved");          // Set return status as approved for partial return
        return orderRepository.save(order);
    }

    // Helper method to calculate refund amount for full return
    private BigDecimal calculateRefundAmount(Orders order) {
        return order.getItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Helper method to calculate refund for partial return
    private BigDecimal calculatePartialRefund(Orders order, List<OrderItem> itemsToReturn) {
        return itemsToReturn.stream()
                .map(item -> (item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO)
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Method to refund the order (can be triggered after return is approved)
    public void processRefund(String orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getReturnStatus().equals("Approved")) {
            throw new RuntimeException("Return not approved for this order");
        }
        // Trigger the refund logic, this might involve updating payment provider, etc.
        // Assuming it's successful, mark as refunded
        order.setReturnStatus("Refunded");
        orderRepository.save(order);
    }

    // Method to apply the coupon discount to the order
    public void applyCouponDiscount(Orders order, String couponCode, BigDecimal totalPrice) {
        Optional<Coupon> couponOptional = couponRepository.findByCode(couponCode);
        if (couponOptional.isPresent()) {
            Coupon coupon = couponOptional.get();
            if (coupon.isActive() && !coupon.isExpired()) {
                BigDecimal discountAmount = calculateDiscount(coupon,totalPrice);
                BigDecimal finalPrice = totalPrice.subtract(discountAmount);
                order.setTotalPrice(finalPrice.doubleValue()); // Apply the discount to total price
            } else {
                throw new RuntimeException("Coupon is invalid or expired");
            }
        } else {
            throw new RuntimeException("Coupon not found");
        }
    }

    // Method to calculate the discount based on coupon type
    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal totalAmount) {
        if (coupon == null || coupon.getType() == null) {
            return BigDecimal.ZERO; // No discount if coupon or type is null
        }
        if ("PERCENTAGE".equals(coupon.getType())) { // Avoids NullPointerException
            return totalAmount.multiply(coupon.getDiscountValue().divide(BigDecimal.valueOf(100)));
        } else if ("FIXED".equals(coupon.getType())) {
            return coupon.getDiscountValue();
        }
        return BigDecimal.ZERO; // Default: No discount
    }

    // Method to save an updated order
    public Orders saveOrder(Orders order) {
        return orderRepository.save(order); // Assuming you're using a repository to save the order
    }

    private void sendOrderConfirmationEmail(Orders order) {
        String subject = "Order Confirmation - " + order.getId();
        String body = "Thank you for your order!\n\n" +
                "Order ID: " + order.getId() + "\n" +
                "Total Cost: " + order.getTotalPrice() + "\n" +
                "Shipping Address: " + order.getShippingAddress() + "\n" +
                "Estimated Delivery Date: " + order.getDeliveryDate();

        emailService.sendEmail(order.getEmail(), subject, body);
    }

    public void sendOrderDispatchEmail(Orders order) {
        String subject = "Your Order Has Been Shipped - " + order.getId();

        String body = "Great news! Your order has been shipped.\n\n" +
                "Order ID: " + order.getId() + "\n" +
                "Expected Delivery: " + order.getDeliveryDate() ;

        emailService.sendEmail(order.getEmail(), subject, body);
    }

    public void sendOrderDeliveredEmail(Orders order) {
        String subject = "Your Order Has Been Delivered - " + order.getId();
        String body = "Your order has been successfully delivered!\n\n" +
                "Order ID: " + order.getId() + "\n\n" +
                "We’d love to hear your feedback! Please leave a review for your products.\n\n" +
                "Review your order: http://onlineshopping.com/review?orderId=" + order.getId();

        emailService.sendEmail(order.getEmail(), subject, body);
    }

    public String generateOrderConfirmationBody(Orders order) {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Hello, ").append(order.getEmail()).append("\n\n");
        emailBody.append("Your order has been successfully placed!\n\n");
        emailBody.append("Order ID: ").append(order.getId()).append("\n");
        emailBody.append("Shipping Address: ").append(order.getShippingAddress()).append("\n");
        emailBody.append("Estimated Delivery Date: ").append(order.getDeliveryDate()).append("\n");

        emailBody.append("Order Details:\n");
        for (OrderItem item : order.getItems()) {
            emailBody.append("Product: ").append(item.getProductName()).append("\n");
            emailBody.append("Quantity: ").append(item.getQuantity()).append("\n");
            emailBody.append("Price: $").append(item.getPrice()).append("\n\n");
        }

        emailBody.append("Total Price: $").append(order.getTotalPrice()).append("\n");
        emailBody.append("\nThank you for shopping with us!");

        return emailBody.toString();
    }
}
