package com.example.ecommerce.controller;

import com.example.ecommerce.dto.LoginCustomerDto;
import com.example.ecommerce.dto.RegisterCustomerDto;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.AuthenticationService;
import com.example.ecommerce.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void register_ReturnsRegisteredCustomer() throws Exception {
        RegisterCustomerDto registerDto = new RegisterCustomerDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");
        registerDto.setFullName("John Doe");
        registerDto.setPhone("1234567890");
        registerDto.setUsername("johndoe");

        Customer registeredCustomer = new Customer();
        registeredCustomer.setEmail(registerDto.getEmail());
        registeredCustomer.setFullName(registerDto.getFullName());
        registeredCustomer.setPhone(registerDto.getPhone());
        registeredCustomer.setUsername(registerDto.getUsername());

        when(authenticationService.signup(any(RegisterCustomerDto.class))).thenReturn(registeredCustomer);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(registeredCustomer.getEmail()))
                .andExpect(jsonPath("$.fullName").value(registeredCustomer.getFullName()))
                .andExpect(jsonPath("$.phone").value(registeredCustomer.getPhone()))
                .andExpect(jsonPath("$.username").value(registeredCustomer.getUsername()));
    }

    @Test
    void login_ReturnsLoginResponse() throws Exception {
        LoginCustomerDto loginDto = new LoginCustomerDto();
        loginDto.setEmail("test@gmail.com");
        loginDto.setPassword("test");

        Customer authenticatedCustomer = new Customer();
        authenticatedCustomer.setEmail(loginDto.getEmail());
        long expiresIn = 3600000L;

        when(authenticationService.authenticate(any(LoginCustomerDto.class))).thenReturn(authenticatedCustomer);
        when(jwtService.getExpirationTime()).thenReturn(expiresIn);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expiresIn").value(expiresIn));
    }
}
