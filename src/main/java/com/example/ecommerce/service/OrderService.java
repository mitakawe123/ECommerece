package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAllOrdersBasedOnCustomer(Long customerId) {
        return this.orderRepository.findByCustomer_Id(customerId);
    }

    public Optional<Order> findOrderById(Long id) {
        return this.orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        return this.orderRepository.save(order);
    }

    public Order updateOrderStatus(Long id, String orderStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(orderStatus);

        return this.orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        this.orderRepository.deleteById(id);
    }
}
