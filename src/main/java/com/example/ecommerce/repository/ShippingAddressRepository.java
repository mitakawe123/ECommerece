package com.example.ecommerce.repository;

import com.example.ecommerce.model.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    // Custom query to find all shipping addresses by customer ID
    @Query("SELECT sa FROM ShippingAddress sa WHERE sa.customer.id = ?1")
    List<ShippingAddress> findAllByCustomerId(Long customerId);

    // Custom query to find all shipping addresses in a specific country
    @Query("SELECT sa FROM ShippingAddress sa WHERE sa.country = ?1")
    List<ShippingAddress> findAllByCountry(String country);

    // Custom query to find addresses in a specific city
    @Query("SELECT sa FROM ShippingAddress sa WHERE sa.city = ?1")
    List<ShippingAddress> findAllByCity(String city);
}
