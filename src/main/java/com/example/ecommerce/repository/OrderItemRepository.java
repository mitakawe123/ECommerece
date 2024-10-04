package com.example.ecommerce.repository;

import com.example.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Custom query to find all order items by order ID
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = ?1")
    List<OrderItem> findAllByOrder_Id(Long orderId);

    // Custom query to find all order items by product ID
    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.id = ?1")
    List<OrderItem> findAllByProduct_Id(Long productId);

    // Custom query to find all order items with a quantity greater than a specific value
    @Query("SELECT oi FROM OrderItem oi WHERE oi.quantity > ?1")
    List<OrderItem> findAllWithQuantityGreaterThan(int quantity);
}
