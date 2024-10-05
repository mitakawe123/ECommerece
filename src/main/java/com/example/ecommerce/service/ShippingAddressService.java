package com.example.ecommerce.service;

import com.example.ecommerce.model.ShippingAddress;
import com.example.ecommerce.repository.ShippingAddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingAddressService {
    private final ShippingAddressRepository shippingAddressRepository;

    public ShippingAddressService(ShippingAddressRepository shippingAddressRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
    }

    public List<ShippingAddress> findAllByCustomerId(Long customerId) {
        return shippingAddressRepository.findAllByCustomer_Id(customerId);
    }

    public Optional<ShippingAddress> findById(Long id) {
        return shippingAddressRepository.findById(id);
    }

    public List<ShippingAddress> findAllByCountry(String country) {
        return shippingAddressRepository.findAllByCountry(country);
    }

    public List<ShippingAddress> findAllByCity(String city) {
        return shippingAddressRepository.findAllByCity(city);
    }

    public ShippingAddress createShippingAddress(ShippingAddress shippingAddress) {
        return shippingAddressRepository.save(shippingAddress);
    }

    public ShippingAddress updateShippingAddress(Long id, ShippingAddress updatedAddress) {
        var existingAddress = shippingAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        existingAddress.setAddressLine1(updatedAddress.getAddressLine1());
        existingAddress.setAddressLine2(updatedAddress.getAddressLine2());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setPostalCode(updatedAddress.getPostalCode());
        existingAddress.setCountry(updatedAddress.getCountry());

        return shippingAddressRepository.save(existingAddress);
    }

    public void deleteShippingAddress(Long id) {
        shippingAddressRepository.deleteById(id);
    }
}
