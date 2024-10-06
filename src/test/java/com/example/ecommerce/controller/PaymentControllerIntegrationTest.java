package com.example.ecommerce.controller;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ecommerce.model.Order;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
class PaymentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    private ObjectMapper objectMapper;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        order = new Order(47L);
        order.setStatus("Pending");
        order.setCreatedAt(Instant.now());
    }

    @Test
    void getAllPaymentsByOrderId_ReturnsPayments() throws Exception {
        Long orderId = order.getId();
        Payment payment1 = new Payment(46L);
        payment1.setAmount(BigDecimal.valueOf(64.19));
        payment1.setOrder(order);

        Payment payment2 = new Payment(47L);
        payment2.setAmount(BigDecimal.valueOf(61.92));
        payment2.setOrder(order);

        when(paymentService.findAllByOrderId(orderId)).thenReturn(Arrays.asList(payment1, payment2));

        mockMvc.perform(get("/api/payments/order/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$[0].id", is((int)payment1.getId())))
                .andExpect(jsonPath("$[0].amount", is(payment1.getAmount().doubleValue())))
                .andExpect(jsonPath("$[1].id", is((int)payment2.getId())))
                .andExpect(jsonPath("$[1].amount", is(payment2.getAmount().doubleValue())));
    }

    @Test
    void getPaymentById_ReturnsPayment() throws Exception {
        Long paymentId = 46L;
        Payment payment = new Payment(paymentId);
        payment.setAmount(BigDecimal.valueOf(64.19));
        payment.setOrder(order);

        when(paymentService.findById(paymentId)).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/api/payments/{id}", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(paymentId.intValue())))
                .andExpect(jsonPath("$.amount", is(payment.getAmount().doubleValue())));
    }

    @Test
    void getPaymentById_ReturnsNotFound() throws Exception {
        Long paymentId = 1L;

        when(paymentService.findById(paymentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/payments/{id}", paymentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPaymentsByStatus_ReturnsPayments() throws Exception {
        String status = "Pending";
        Payment payment = new Payment(46L);
        payment.setStatus(status);
        payment.setOrder(order); // Set the Order for the Payment

        when(paymentService.findAllByStatus(status)).thenReturn(Collections.singletonList(payment));

        mockMvc.perform(get("/api/payments/status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is((int) payment.getId())))
                .andExpect(jsonPath("$[0].status", is(payment.getStatus())));
    }

    @Test
    void createPayment_ReturnsCreated() throws Exception {
        Payment payment = new Payment(46L);
        payment.setAmount(BigDecimal.valueOf(100.0));
        payment.setOrder(order);

        when(paymentService.createPayment(any(Payment.class))).thenReturn(payment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount", is(payment.getAmount().doubleValue())));
    }

    @Test
    void updatePayment_ReturnsUpdatedPayment() throws Exception {
        Long paymentId = 46L;

        Payment updatedPayment = new Payment(paymentId);
        updatedPayment.setAmount(BigDecimal.valueOf(150.0));
        updatedPayment.setOrder(order);

        when(paymentService.updatePayment(eq(paymentId), any(Payment.class))).thenReturn(updatedPayment);

        mockMvc.perform(put("/api/payments/{id}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPayment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(updatedPayment.getAmount().doubleValue())));
    }

    @Test
    void deletePayment_ReturnsNoContent() throws Exception {
        Long paymentId = 1L;

        doNothing().when(paymentService).deletePayment(paymentId);

        mockMvc.perform(delete("/api/payments/{id}", paymentId))
                .andExpect(status().isNoContent());
    }
}
