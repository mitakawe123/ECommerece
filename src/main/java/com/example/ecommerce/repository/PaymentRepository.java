package com.example.ecommerce.repository;

import com.example.ecommerce.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Custom query to find payments by order ID
    @Query("SELECT p FROM Payment p WHERE p.order.id = ?1")
    List<Payment> findAllByOrder_Id(Long orderId);

    // Custom query to find all payments by status
    @Query("SELECT p FROM Payment p WHERE p.status = ?1")
    List<Payment> findAllByStatus(String status);

    // Custom query to find payments within a specific amount range
    @Query("SELECT p FROM Payment p WHERE p.amount BETWEEN ?1 AND ?2")
    List<Payment> findAllByAmountBetween(Double minAmount, Double maxAmount);
}
