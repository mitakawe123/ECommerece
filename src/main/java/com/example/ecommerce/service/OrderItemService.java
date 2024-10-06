package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderItemDto;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));
    }

    public List<OrderItem> findAllByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrder_Id(orderId);
    }

    public List<OrderItem> findAllByProductId(Long productId) {
        return orderItemRepository.findAllByProduct_Id(productId);
    }

    public List<OrderItem> findAllWithQuantityGreaterThan(int quantity) {
        return orderItemRepository.findAllWithQuantityGreaterThan(quantity);
    }

    public OrderItem updateOrderItemQuantity(Long orderItemId, int quantity) {
        var orderItem = findById(orderItemId);
        orderItem.setQuantity(quantity);
        return orderItemRepository.save(orderItem);
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }

    public OrderItemDto convertToDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();

        var orderId = orderRepository.findById(orderItem.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        var productId = productRepository.findById(orderItem.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        dto.setOrderId(orderId.getId());
        dto.setProductId(productId.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        return dto;
    }

    public OrderItem convertToEntity(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(orderItemDto.getPrice());

        var order = orderRepository.findById(orderItemDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        var product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        orderItem.setOrder(order);
        orderItem.setProduct(product);

        return orderItem;
    }
}
