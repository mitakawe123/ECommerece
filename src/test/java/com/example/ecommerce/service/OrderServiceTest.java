package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllOrdersBasedOnCustomer_ValidCustomerId_ReturnsOrderList() {
        Long customerId = 1L;
        Order order = new Order();
        order.setStatus("Completed");

        when(orderRepository.findByCustomer_Id(customerId)).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.findAllOrdersBasedOnCustomer(customerId);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order, orders.getFirst());
        verify(orderRepository, times(1)).findByCustomer_Id(customerId);
    }

    @Test
    void findOrderById_ValidId_ReturnsOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setStatus("Completed");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.findOrderById(orderId);

        assertTrue(foundOrder.isPresent());
        assertEquals(order, foundOrder.get());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void findOrderById_InvalidId_ReturnsEmptyOptional() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<Order> foundOrder = orderService.findOrderById(orderId);

        assertFalse(foundOrder.isPresent());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void createOrder_ValidOrder_ReturnsSavedOrder() {
        Order order = new Order();
        order.setStatus("Pending");

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder);
        assertEquals("Pending", savedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateOrderStatus_ValidId_UpdatesAndReturnsOrder() {
        Long orderId = 1L;
        Order existingOrder = new Order();
        existingOrder.setStatus("Pending");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        String newStatus = "Completed";
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);

        assertNotNull(updatedOrder);
        assertEquals(newStatus, updatedOrder.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    void updateOrderStatus_InvalidId_ThrowsException() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            orderService.updateOrderStatus(orderId, "Completed");
        });

        assertEquals("Order not found", thrown.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void deleteOrderById_ValidId_CallsDelete() {
        Long orderId = 1L;

        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrderById(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}