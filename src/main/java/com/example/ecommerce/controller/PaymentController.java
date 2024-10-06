package com.example.ecommerce.controller;

import com.example.ecommerce.dto.PaymentDto;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getAllPaymentsByOrderId(@PathVariable Long orderId) {
        List<Payment> payments = paymentService.findAllByOrderId(orderId);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.findById(id);
        return payment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable String status) {
        return new ResponseEntity<>(paymentService.findAllByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/amount")
    public ResponseEntity<List<Payment>> getPaymentsByAmountBetween(@RequestParam Double min, @RequestParam Double max) {
        return new ResponseEntity<>(paymentService.findAllByAmountBetween(min, max), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDto paymentDto) {
        Payment payment = convertToEntity(paymentDto);
        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable Long id, @RequestBody PaymentDto updatedPaymentDto) {
        Payment updatedPayment = convertToEntity(updatedPaymentDto);
        Payment payment = paymentService.updatePayment(id, updatedPayment);
        PaymentDto updatedPaymentDtoResponse = paymentService.convertToDto(payment);
        return ResponseEntity.ok(updatedPaymentDtoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    private Payment convertToEntity(PaymentDto paymentDto) {
        Payment payment = new Payment(paymentDto.getId());
        payment.setAmount(paymentDto.getAmount());
        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setStatus(paymentDto.getStatus());

        if (paymentDto.getOrderId() != null) {
            Order order = orderService.findOrderById(paymentDto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            payment.setOrder(order);
        }

        return payment;
    }
}
