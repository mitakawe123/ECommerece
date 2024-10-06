package com.example.ecommerce.controller;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.ShippingAddress;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ShippingAddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
class ShippingAddressControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(33L);
        customer.setUsername("test_username");
        customer.setFullName("Ivan Georgiev");
        customer.setEmail("test-main@gmail.com");
        customer.setPassword("password-strong");
        customer.setPhone("0893215373231");
        customerRepository.save(customer);
    }

    @Test
    void getAllShippingAddressesByCustomerId_ReturnsAddresses() throws Exception {
        ShippingAddress shippingAddress = new ShippingAddress(15L);
        shippingAddress.setAddressLine1("Test Address Line 1");
        shippingAddress.setCity("Test City");
        shippingAddress.setCountry("Test Country");
        shippingAddress.setPostalCode("12345");
        shippingAddress.setState("Test State");
        shippingAddress.setCustomer(customer);
        shippingAddressRepository.save(shippingAddress);

        mockMvc.perform(get("/api/shipping-addresses/customer/{customerId}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void getShippingAddressById_ReturnsAddress() throws Exception {
        ShippingAddress shippingAddress = new ShippingAddress(33L);
        shippingAddress.setAddressLine1("123 New St");
        shippingAddress.setCity("Test City");
        shippingAddress.setCustomer(customer);
        shippingAddressRepository.save(shippingAddress);

        mockMvc.perform(get("/api/shipping-addresses/{id}", shippingAddress.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is((int)shippingAddress.getCustomer().getId())));
    }

    @Test
    void createShippingAddress_ReturnsCreated() throws Exception {
        String newAddress = """
            {
                "addressLine1": "123 New St",
                "city": "New City",
                "country": "New Country",
                "postalCode": "10000",
                "state": "State1",
                "customerId": %d
            }
            """.formatted(customer.getId());

        mockMvc.perform(post("/api/shipping-addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newAddress))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.addressLine1", is("123 New St")))
                .andExpect(jsonPath("$.city", is("New City")));
    }

    @Test
    void updateShippingAddress_UpdatesAndReturnsAddress() throws Exception {
        ShippingAddress shippingAddress = new ShippingAddress(33L);
        shippingAddress.setAddressLine1("Old Address");
        shippingAddress.setCity("Old City");
        shippingAddress.setCountry("Old Country");
        shippingAddress.setPostalCode("Old PostalCode");
        shippingAddress.setState("Old State");
        shippingAddress.setCustomer(customer);
        shippingAddressRepository.save(shippingAddress);

        String updatedAddress = """
            {
                "addressLine1": "Updated Address",
                "city": "City1",
                "country": "Country2",
                "postalCode": "Postal1",
                "state": "State2"
            }
            """;

        mockMvc.perform(put("/api/shipping-addresses/{id}", shippingAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressLine1", is("Updated Address")));
    }

    @Test
    void deleteShippingAddress_DeletesAddress() throws Exception {
        ShippingAddress shippingAddress = new ShippingAddress(33L);
        shippingAddress.setAddressLine1("Address to Delete");
        shippingAddress.setCustomer(customer);
        shippingAddressRepository.save(shippingAddress);

        mockMvc.perform(delete("/api/shipping-addresses/{id}", shippingAddress.getId()))
                .andExpect(status().isNoContent());
    }
}
