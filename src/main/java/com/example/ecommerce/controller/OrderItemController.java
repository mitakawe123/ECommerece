package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderItemDto;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.service.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable Long id) {
        OrderItem orderItem = orderItemService.findById(id);
        return ResponseEntity.ok(orderItemService.convertToDto(orderItem));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderItemService.findAllByOrderId(orderId);
        return ResponseEntity.ok(orderItems.stream().map(orderItemService::convertToDto).toList());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByProductId(@PathVariable Long productId) {
        List<OrderItem> orderItems = orderItemService.findAllByProductId(productId);
        return ResponseEntity.ok(orderItems.stream().map(orderItemService::convertToDto).toList());
    }

    @GetMapping("/quantity/{quantity}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsWithQuantityGreaterThan(@PathVariable int quantity) {
        List<OrderItem> orderItems = orderItemService.findAllWithQuantityGreaterThan(quantity);
        return ResponseEntity.ok(orderItems.stream().map(orderItemService::convertToDto).toList());
    }

    @PostMapping
    public ResponseEntity<OrderItemDto> createOrderItem(@RequestBody OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemService.convertToEntity(orderItemDto);
        OrderItem createdOrderItem = orderItemService.createOrderItem(orderItem);
        return new ResponseEntity<>(orderItemService.convertToDto(createdOrderItem), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> updateOrderItemQuantity(@PathVariable Long id, @RequestParam int quantity) {
        OrderItem updatedOrderItem = orderItemService.updateOrderItemQuantity(id, quantity);
        return ResponseEntity.ok(orderItemService.convertToDto(updatedOrderItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}