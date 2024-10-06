package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.dto.UpdateOrderDto;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.findById(customerId);
        List<Order> orders = orderService.findAllOrdersBasedOnCustomer(customer.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Optional<Order> order = orderService.findOrderById(orderId);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto) {
        var customer = customerService.findById(orderDto.getCustomerId());

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(orderDto.getStatus());
        order.setTotal(orderDto.getTotal());
        order.setOrderDate(orderDto.getOrderDate() != null ? orderDto.getOrderDate() : Instant.now());

        Order createdOrder = this.orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderDto orderDto) {
        Order updatedOrder = this.orderService.updateOrderStatus(orderId, orderDto.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long orderId) {
        this.orderService.deleteOrderById(orderId);
        return ResponseEntity.noContent().build();
    }
}
