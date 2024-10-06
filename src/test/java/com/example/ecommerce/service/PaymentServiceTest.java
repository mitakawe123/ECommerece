package com.example.ecommerce.service;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllByOrderId_ReturnsListOfPayments() {
        Long orderId = 1L;
        Payment payment1 = new Payment();
        payment1.setAmount(BigDecimal.valueOf(100.0));
        payment1.setStatus("Completed");

        Payment payment2 = new Payment();
        payment2.setAmount(BigDecimal.valueOf(200.0));
        payment2.setStatus("Pending");

        when(paymentRepository.findAllByOrder_Id(orderId)).thenReturn(Arrays.asList(payment1, payment2));

        List<Payment> payments = paymentService.findAllByOrderId(orderId);

        assertNotNull(payments);
        assertEquals(2, payments.size());
        assertEquals(BigDecimal.valueOf(100.0), payments.get(0).getAmount());
        assertEquals(BigDecimal.valueOf(200.0), payments.get(1).getAmount());
    }

    @Test
    void findById_ValidId_ReturnsPayment() {
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100.0));
        payment.setStatus("Completed");

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        Optional<Payment> foundPayment = paymentService.findById(paymentId);

        assertTrue(foundPayment.isPresent());
        assertEquals(BigDecimal.valueOf(100.0), foundPayment.get().getAmount());
    }

    @Test
    void findById_InvalidId_ReturnsEmpty() {
        Long paymentId = 1L;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        Optional<Payment> foundPayment = paymentService.findById(paymentId);

        assertFalse(foundPayment.isPresent());
    }

    @Test
    void findAllByStatus_ReturnsListOfPayments() {
        String status = "Completed";
        Payment payment1 = new Payment();
        payment1.setAmount(BigDecimal.valueOf(100.0));
        payment1.setStatus(status);

        Payment payment2 = new Payment();
        payment2.setAmount(BigDecimal.valueOf(200.0));
        payment2.setStatus(status);

        when(paymentRepository.findAllByStatus(status)).thenReturn(Arrays.asList(payment1, payment2));

        List<Payment> payments = paymentService.findAllByStatus(status);

        assertNotNull(payments);
        assertEquals(2, payments.size());
    }

    @Test
    void findAllByAmountBetween_ReturnsListOfPayments() {
        Double minAmount = 100.0;
        Double maxAmount = 200.0;
        Payment payment1 = new Payment();
        payment1.setAmount(BigDecimal.valueOf(150.0));
        payment1.setStatus("Completed");

        Payment payment2 = new Payment();
        payment2.setAmount(BigDecimal.valueOf(180.0));
        payment2.setStatus("Pending");

        when(paymentRepository.findAllByAmountBetween(minAmount, maxAmount))
                .thenReturn(Arrays.asList(payment1, payment2));

        List<Payment> payments = paymentService.findAllByAmountBetween(minAmount, maxAmount);

        assertNotNull(payments);
        assertEquals(2, payments.size());
    }

    @Test
    void createPayment_SavesAndReturnsPayment() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100.0));
        payment.setStatus("Completed");

        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment createdPayment = paymentService.createPayment(payment);

        assertNotNull(createdPayment);
        assertEquals(BigDecimal.valueOf(100.0), createdPayment.getAmount());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void updatePayment_ValidId_UpdatesAndReturnsPayment() {
        Long paymentId = 1L;

        Payment existingPayment = new Payment();
        existingPayment.setAmount(BigDecimal.valueOf(100.0));
        existingPayment.setStatus("Completed");

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(existingPayment));

        Payment updatedPayment = new Payment();
        updatedPayment.setAmount(BigDecimal.valueOf(150.0));
        updatedPayment.setStatus("Pending");

        when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        Payment result = paymentService.updatePayment(paymentId, updatedPayment);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150.0), result.getAmount());
        assertEquals("Pending", result.getStatus());

        verify(paymentRepository, times(1)).save(existingPayment);
    }

    @Test
    void updatePayment_InvalidId_ThrowsRuntimeException() {
        Long paymentId = 1L;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        Payment updatedPayment = new Payment();
        updatedPayment.setAmount(BigDecimal.valueOf(150.0));

        assertThrows(RuntimeException.class, () -> {
            paymentService.updatePayment(paymentId, updatedPayment);
        });
    }

    @Test
    void deletePayment_ValidId_DeletesPayment() {
        Long paymentId = 1L;

        paymentService.deletePayment(paymentId);

        verify(paymentRepository, times(1)).deleteById(paymentId);
    }
}