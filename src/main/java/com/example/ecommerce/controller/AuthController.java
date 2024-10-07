package com.example.ecommerce.controller;

import com.example.ecommerce.dto.LoginCustomerDto;
import com.example.ecommerce.dto.RegisterCustomerDto;
import com.example.ecommerce.dto.ResponseDto;
import com.example.ecommerce.exception.AuthenticationException;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.response.LoginResponse;
import com.example.ecommerce.service.AuthenticationService;
import com.example.ecommerce.service.JwtService;

import org.hibernate.type.CustomType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<Customer>> register(@RequestBody RegisterCustomerDto registerUserDto) {
        try {
            var registeredUser = authenticationService.signup(registerUserDto);
            var response = new ResponseDto<Customer>(registeredUser, null);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            var response = new ResponseDto<Customer>(null, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginCustomerDto loginUserDto) {
        Customer authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
