package com.vinno.osa.repository;

import com.vinno.osa.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {


    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findByBrand(String brand);
    List<Product> findByCategory(String categories);

    List<Product> findByCategoriesContaining(String category);


    Optional<Product> findById(String productId);

    List<Product> findByCategoryIn(List<String> categories);

    @Query("{ $text: { $search: ?0 } }")
    List<Product> searchProducts(String keyword);

    List<Product> findByTrendingTrue();

//    Page<Product> findByCategoryIgnoreCaseContainingAndBrandIgnoreCaseContainingAndColorIgnoreCaseContainingAndSizeContainingAndPriceBetweenAndRatingGreaterThanEqual(
//            String category, String brand, String color, String size,
//            BigDecimal minPrice, BigDecimal maxPrice, Double rating, Pageable pageable);

    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("{ '_id': { $in: ?0 }, $or: [ { 'onSale': true }, { 'stock': { $lt: 5 } } ] }")
    List<Product> findByIdInAndOnSaleOrLowStock(List<String> productIds);

    @Query("{ 'categories': { $in: ?0 }, 'price': { $gte: ?1, $lte: ?2 } }")
    List<Product> findByCategoriesAndPriceRange(List<String> categories, BigDecimal minPrice, BigDecimal maxPrice);
}
