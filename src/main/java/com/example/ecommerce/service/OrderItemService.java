package com.example.ecommerce.service;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found"));
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
}
