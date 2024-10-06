package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ShippingAddressDto;
import com.example.ecommerce.service.ShippingAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping-addresses")
public class ShippingAddressController {
    private final ShippingAddressService shippingAddressService;

    public ShippingAddressController(ShippingAddressService shippingAddressService) {
        this.shippingAddressService = shippingAddressService;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ShippingAddressDto>> getAllShippingAddressesByCustomerId(@PathVariable Long customerId) {
        List<ShippingAddressDto> addresses = shippingAddressService.findAllByCustomerId(customerId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingAddressDto> getShippingAddressById(@PathVariable Long id) {
        return shippingAddressService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<ShippingAddressDto>> getAddressesByCountry(@PathVariable String country) {
        List<ShippingAddressDto> addresses = shippingAddressService.findAllByCountry(country);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<ShippingAddressDto>> getAddressesByCity(@PathVariable String city) {
        List<ShippingAddressDto> addresses = shippingAddressService.findAllByCity(city);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<ShippingAddressDto> createShippingAddress(@RequestBody ShippingAddressDto shippingAddressDto) {
        ShippingAddressDto createdAddress = shippingAddressService.createShippingAddress(shippingAddressDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingAddressDto> updateShippingAddress(@PathVariable Long id, @RequestBody ShippingAddressDto updatedDto) {
        ShippingAddressDto address = shippingAddressService.updateShippingAddress(id, updatedDto);
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShippingAddress(@PathVariable Long id) {
        shippingAddressService.deleteShippingAddress(id);
        return ResponseEntity.noContent().build();
    }
}