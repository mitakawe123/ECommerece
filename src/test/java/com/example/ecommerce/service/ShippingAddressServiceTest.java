package com.example.ecommerce.service;

import com.example.ecommerce.dto.ShippingAddressDto;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.ShippingAddress;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ShippingAddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShippingAddressServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @InjectMocks
    private ShippingAddressService shippingAddressService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer(1L);
        customer.setFullName("Test Customer");
        customer.setEmail("test@test.com");
        customer.setPassword("test123");
        customer.setPhone("08796876876576");
        customer.setUsername("customer123");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
    }

    @Test
    void findAllByCustomerId_ValidId_ReturnsShippingAddresses() {
        Long customerId = 1L;
        ShippingAddress address1 = new ShippingAddress();
        address1.setAddressLine1("123 Main St");
        address1.setCustomer(customer);
        ShippingAddress address2 = new ShippingAddress();
        address2.setAddressLine1("456 Elm St");
        address2.setCustomer(customer);

        when(shippingAddressRepository.findAllByCustomerId(customerId))
                .thenReturn(Arrays.asList(address1, address2));

        var addresses = shippingAddressService.findAllByCustomerId(customerId);

        assertNotNull(addresses);
        assertEquals(2, addresses.size());
    }

    @Test
    void findById_ValidId_ReturnsShippingAddress() {
        Long addressId = 1L;
        ShippingAddress address = new ShippingAddress();
        address.setAddressLine1("123 Main St");
        address.setCustomer(customer);  // Set the customer for the address

        when(shippingAddressRepository.findById(addressId)).thenReturn(Optional.of(address));

        var foundAddress = shippingAddressService.findById(addressId);

        assertTrue(foundAddress.isPresent());
        assertEquals("123 Main St", foundAddress.get().getAddressLine1());
    }

    @Test
    void findById_InvalidId_ReturnsEmptyOptional() {
        Long addressId = 1L;
        when(shippingAddressRepository.findById(addressId)).thenReturn(Optional.empty());

        var foundAddress = shippingAddressService.findById(addressId);

        assertFalse(foundAddress.isPresent());
    }

    @Test
    void findAllByCountry_ValidCountry_ReturnsShippingAddresses() {
        String country = "USA";
        ShippingAddress address = new ShippingAddress();
        address.setCountry(country);
        address.setCustomer(customer);

        when(shippingAddressRepository.findAllByCountry(country)).thenReturn(List.of(address));

        var addresses = shippingAddressService.findAllByCountry(country);

        assertNotNull(addresses);
        assertEquals(1, addresses.size());
        assertEquals("USA", addresses.getFirst().getCountry());
    }

    @Test
    void findAllByCity_ValidCity_ReturnsShippingAddresses() {
        String city = "Los Angeles";
        ShippingAddress address = new ShippingAddress();
        address.setCity(city);
        address.setCustomer(customer);

        when(shippingAddressRepository.findAllByCity(city)).thenReturn(List.of(address));

        var addresses = shippingAddressService.findAllByCity(city);

        assertNotNull(addresses);
        assertEquals(1, addresses.size());
        assertEquals("Los Angeles", addresses.getFirst().getCity());
    }

    @Test
    void createShippingAddress_SavesAndReturnsShippingAddress() {
        var addressDto = new ShippingAddressDto();
        addressDto.setAddressLine1("123 Main St");
        addressDto.setCity("New York");
        addressDto.setState("NY");
        addressDto.setPostalCode("10001");
        addressDto.setCountry("USA");
        addressDto.setCustomerId(1L);

        var addressEntity = new ShippingAddress();
        addressEntity.setAddressLine1("123 Main St");
        addressEntity.setCity("New York");
        addressEntity.setState("NY");
        addressEntity.setPostalCode("10001");
        addressEntity.setCountry("USA");
        addressEntity.setCustomer(customer);

        when(shippingAddressRepository.save(any(ShippingAddress.class))).thenReturn(addressEntity);

        var createdAddress = shippingAddressService.createShippingAddress(addressDto);

        assertNotNull(createdAddress);
        assertEquals("123 Main St", createdAddress.getAddressLine1());
        assertEquals("New York", createdAddress.getCity());
        assertEquals("NY", createdAddress.getState());
        assertEquals("10001", createdAddress.getPostalCode());
        assertEquals("USA", createdAddress.getCountry());

        verify(shippingAddressRepository, times(1)).save(any(ShippingAddress.class));
    }

    @Test
    void updateShippingAddress_ValidId_UpdatesAndReturnsShippingAddress() {
        Long addressId = 1L;
        var existingAddress = new ShippingAddress();
        existingAddress.setAddressLine1("Old Address");
        existingAddress.setCustomer(customer);  // Set the customer for the address

        when(shippingAddressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));

        var updatedAddress = new ShippingAddressDto();
        updatedAddress.setAddressLine1("New Address");

        when(shippingAddressRepository.save(existingAddress)).thenReturn(existingAddress);

        var result = shippingAddressService.updateShippingAddress(addressId, updatedAddress);

        assertNotNull(result);
        assertEquals("New Address", result.getAddressLine1());

        verify(shippingAddressRepository, times(1)).save(existingAddress);
    }

    @Test
    void updateShippingAddress_InvalidId_ThrowsRuntimeException() {
        Long addressId = 1L;
        when(shippingAddressRepository.findById(addressId)).thenReturn(Optional.empty());

        var updatedAddress = new ShippingAddressDto();
        updatedAddress.setAddressLine1("New Address");

        assertThrows(RuntimeException.class, () -> {
            shippingAddressService.updateShippingAddress(addressId, updatedAddress);
        });
    }

    @Test
    void deleteShippingAddress_ValidId_DeletesAddress() {
        Long addressId = 1L;
        ShippingAddress address = new ShippingAddress();
        address.setAddressLine1("123 Main St");
        address.setCustomer(customer);  // Set the customer for the address

        when(shippingAddressRepository.findById(addressId)).thenReturn(Optional.of(address));

        shippingAddressService.deleteShippingAddress(addressId);

        verify(shippingAddressRepository, times(1)).deleteById(addressId);
    }
}