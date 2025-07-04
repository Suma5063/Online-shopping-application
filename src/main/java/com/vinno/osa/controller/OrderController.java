package com.vinno.osa.controller;
import com.vinno.osa.dto.OrderStatusUpdateRequest;
import com.vinno.osa.entity.*;
import com.vinno.osa.repository.CouponRepository;
import com.vinno.osa.service.JwtService;
import com.vinno.osa.service.OrderService;
import com.vinno.osa.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PaymentService paymentService;


    // Get order by ID
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<Orders> getOrderById(@PathVariable String orderId) {
        Orders order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

  @GetMapping("/user/{userId}")
  @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
  public ResponseEntity<List<Orders>> getOrdersByUserId(HttpServletRequest request) {
      String authHeader = request.getHeader("Authorization");
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }
      String token = authHeader.substring(7);  // Remove "Bearer " prefix
      String userEmail = jwtService.extractUsername(token);  // Extract email from JWT token
      // Fetch orders by user email
      List<Orders> orders = orderService.getOrdersByUserEmail(userEmail);
      return ResponseEntity.ok(orders);
  }

    // Update the status of an order
    @PutMapping("/update/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Orders> updateOrderStatus(@PathVariable String orderId, @RequestBody OrderStatusUpdateRequest request) {
        Orders updatedOrder = orderService.updateOrderStatus(orderId, request.getNewStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    // Cancel an order
    @DeleteMapping("/cancel/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Orders> cancelOrder(@PathVariable String orderId) {
        Orders canceledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(canceledOrder);
    }

    // Endpoint to initiate a return request
    @PostMapping("/{orderId}/return")
    @PreAuthorize("hasRole('BUYER')")
    public Orders requestReturn(@PathVariable String orderId, @RequestParam String returnReason) {
        return orderService.requestReturn(orderId, returnReason);
    }

    // Endpoint to approve a return
    @PostMapping("/{orderId}/approve-return")
    @PreAuthorize("hasRole('ADMIN')")
    public Orders approveReturn(@PathVariable String orderId) {
        return orderService.approveReturn(orderId);
    }

    // Endpoint to process a partial return
    @PostMapping("/{orderId}/partial-return")
    @PreAuthorize("hasRole('ADMIN')")
    public Orders processPartialReturn(@PathVariable String orderId, @RequestBody List<OrderItem> itemsToReturn) {
        return orderService.processPartialReturn(orderId, itemsToReturn);
    }

    // Endpoint to process the refund after return approval
    @PostMapping("/{orderId}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public void processRefund(@PathVariable String orderId) {
        orderService.processRefund(orderId);
    }


    // Endpoint to apply the coupon discount to an order
    @PutMapping("/{orderId}/apply-coupon")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> applyCouponToOrder(@PathVariable String orderId,
                                                     @RequestParam String couponCode) {
        try {

            Orders order = orderService.getOrderById(orderId);                    // Retrieve the order from the service layer
            BigDecimal totalPrice = BigDecimal.valueOf(order.getTotalPrice());    // Assuming total price is passed from the frontend or can be calculated
            orderService.applyCouponDiscount(order, couponCode, totalPrice);      // Apply coupon discount to the order
            orderService.saveOrder(order);                                        // Save the updated order

            return ResponseEntity.ok("Coupon applied successfully! New total: " + order.getTotalPrice());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Endpoint to validate the coupon (check if it's active and not expired)
    @GetMapping("/validate-coupon")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<String> validateCoupon(@RequestParam String couponCode) {
        Optional<Coupon> couponOptional = couponRepository.findByCode(couponCode);

        if (couponOptional.isPresent()) {
            Coupon coupon = couponOptional.get();
            if (coupon.isActive() && !coupon.isExpired()) {
                return ResponseEntity.ok("Coupon is valid!");
            } else {
                return ResponseEntity.badRequest().body("Coupon is invalid or expired.");
            }
        } else {
            return ResponseEntity.badRequest().body("Coupon not found.");
        }
    }


    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/place")
    public ResponseEntity<Orders> placeOrder() {
        Orders order = orderService.placeOrder();
        order.setUserId(order.getUserId());
        order.setEmail(order.getEmail());
        order.setShippingAddress(order.getShippingAddress());
        order.setUpdatedAt(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryDate(String.valueOf(LocalDateTime.now().plusDays(8)));
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/fail")
    public ResponseEntity<String> handlePaymentFailure(@RequestParam String orderId,
                                                       @RequestParam String failureReason) {
        // Call the payment service to handle the failure and send an email
        paymentService.handlePaymentFailure(orderId, failureReason);

        return ResponseEntity.ok("Payment failure processed and email sent.");
    }

}
