package com.example.ecommerce.service;

import com.example.ecommerce.dto.PaymentDto;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findAllByOrderId(Long orderId) {
        return paymentRepository.findAllByOrder_Id(orderId);
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> findAllByStatus(String status) {
        return paymentRepository.findAllByStatus(status);
    }

    public List<Payment> findAllByAmountBetween(Double minAmount, Double maxAmount) {
        return paymentRepository.findAllByAmountBetween(minAmount, maxAmount);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment updatedPayment) {
        var existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        existingPayment.setAmount(updatedPayment.getAmount());
        existingPayment.setPaymentDate(updatedPayment.getPaymentDate());
        existingPayment.setPaymentMethod(updatedPayment.getPaymentMethod());
        existingPayment.setStatus(updatedPayment.getStatus());

        return paymentRepository.save(existingPayment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public PaymentDto convertToDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmount(payment.getAmount());
        paymentDto.setPaymentDate(payment.getPaymentDate());
        paymentDto.setPaymentMethod(payment.getPaymentMethod());
        paymentDto.setStatus(payment.getStatus());
        return paymentDto;
    }

}
