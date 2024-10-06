package com.example.ecommerce.service;

import com.example.ecommerce.dto.ShippingAddressDto;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.ShippingAddress;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ShippingAddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShippingAddressService {
    private final ShippingAddressRepository shippingAddressRepository;
    private final CustomerRepository customerRepository;

    public ShippingAddressService(ShippingAddressRepository shippingAddressRepository, CustomerRepository customerRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
        this.customerRepository = customerRepository;
    }

    public List<ShippingAddressDto> findAllByCustomerId(Long customerId) {
        List<ShippingAddress> addresses = shippingAddressRepository.findAllByCustomerId(customerId);
        return addresses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ShippingAddressDto> findById(Long id) {
        return shippingAddressRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<ShippingAddressDto> findAllByCountry(String country) {
        return shippingAddressRepository.findAllByCountry(country).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ShippingAddressDto> findAllByCity(String city) {
        return shippingAddressRepository.findAllByCity(city).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ShippingAddressDto createShippingAddress(ShippingAddressDto dto) {
        ShippingAddress address = convertToEntity(dto);
        ShippingAddress savedAddress = shippingAddressRepository.save(address);
        return convertToDto(savedAddress);
    }

    public ShippingAddressDto updateShippingAddress(Long id, ShippingAddressDto dto) {
        ShippingAddress address = shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address not found"));
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        shippingAddressRepository.save(address);
        return convertToDto(address);
    }

    public void deleteShippingAddress(Long id) {
        shippingAddressRepository.deleteById(id);
    }

    private ShippingAddressDto convertToDto(ShippingAddress address) {
        ShippingAddressDto dto = new ShippingAddressDto();
        dto.setCustomerId(address.getCustomer().getId());
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        return dto;
    }

    private ShippingAddress convertToEntity(ShippingAddressDto dto) {
        ShippingAddress address = new ShippingAddress();
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        address.setCustomer(customer);
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        return address;
    }
}