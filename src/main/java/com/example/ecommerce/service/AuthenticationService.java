package com.example.ecommerce.service;

import com.example.ecommerce.dto.LoginCustomerDto;
import com.example.ecommerce.dto.RegisterCustomerDto;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            CustomerRepository customerRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer signup(RegisterCustomerDto input) {
        Customer customer = new Customer();

        customer.setFullName(input.getFullName());
        customer.setEmail(input.getEmail());
        customer.setPassword(passwordEncoder.encode(input.getPassword()));
        customer.setPhone(input.getPhone());
        customer.setUsername(input.getUsername());

        return customerRepository.save(customer);
    }

    public Customer authenticate(LoginCustomerDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return customerRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
