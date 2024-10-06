package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderItemServiceTest {
    private final OrderItemRepository orderItemRepository = mock(OrderItemRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final OrderItemService orderItemService = new OrderItemService(orderItemRepository, orderRepository, productRepository);

    @Test
    void findById_ValidId_ReturnsOrderItem() {
        Long orderItemId = 1L;
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItem));

        OrderItem result = orderItemService.findById(orderItemId);

        assertNotNull(result);
        verify(orderItemRepository, times(1)).findById(orderItemId);
    }

    @Test
    void findAllByOrderId_ValidId_ReturnsOrderItems() {
        Long orderId = 1L;
        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();
        when(orderItemRepository.findAllByOrder_Id(orderId)).thenReturn(Arrays.asList(orderItem1, orderItem2));

        List<OrderItem> result = orderItemService.findAllByOrderId(orderId);

        assertEquals(2, result.size());
        verify(orderItemRepository, times(1)).findAllByOrder_Id(orderId);
    }

    @Test
    void findAllByProductId_ValidId_ReturnsOrderItems() {
        Long productId = 1L;
        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();
        when(orderItemRepository.findAllByProduct_Id(productId)).thenReturn(Arrays.asList(orderItem1, orderItem2));

        List<OrderItem> result = orderItemService.findAllByProductId(productId);

        assertEquals(2, result.size());
        verify(orderItemRepository, times(1)).findAllByProduct_Id(productId);
    }

    @Test
    void findAllWithQuantityGreaterThan_ValidQuantity_ReturnsOrderItems() {
        int quantity = 5;
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(10);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(6);
        when(orderItemRepository.findAllWithQuantityGreaterThan(quantity)).thenReturn(Arrays.asList(orderItem1, orderItem2));

        List<OrderItem> result = orderItemService.findAllWithQuantityGreaterThan(quantity);

        assertEquals(2, result.size());
        verify(orderItemRepository, times(1)).findAllWithQuantityGreaterThan(quantity);
    }

    @Test
    void updateOrderItemQuantity_ValidId_UpdatesAndReturnsOrderItem() {
        Long orderItemId = 1L;
        OrderItem existingOrderItem = new OrderItem();
        existingOrderItem.setQuantity(5);
        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(existingOrderItem));
        when(orderItemRepository.save(existingOrderItem)).thenReturn(existingOrderItem);

        int newQuantity = 10;
        OrderItem updatedOrderItem = orderItemService.updateOrderItemQuantity(orderItemId, newQuantity);

        assertNotNull(updatedOrderItem);
        assertEquals(newQuantity, updatedOrderItem.getQuantity());
        verify(orderItemRepository, times(1)).findById(orderItemId);
        verify(orderItemRepository, times(1)).save(existingOrderItem);
    }

    @Test
    void createOrderItem_ValidOrderItem_ReturnsSavedOrderItem() {
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        OrderItem result = orderItemService.createOrderItem(orderItem);

        assertNotNull(result);
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void deleteOrderItem_ValidId_CallsDeleteById() {
        Long orderItemId = 1L;

        orderItemService.deleteOrderItem(orderItemId);

        verify(orderItemRepository, times(1)).deleteById(orderItemId);
    }
}