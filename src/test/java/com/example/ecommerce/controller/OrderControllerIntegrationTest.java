package com.example.ecommerce.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
public class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerService customerService;

    private Customer mockCustomer;
    private Order mockOrder;

    @BeforeEach
    public void setUp() {
        mockCustomer = new Customer(33L);
        mockCustomer.setFullName("John Doe");
        mockCustomer.setEmail("john.doe@example.com");
        mockCustomer.setPhone("0768685765");
        mockCustomer.setPassword("password");
        mockCustomer.setUsername("john.doe");
        mockCustomer.setCreatedAt(Instant.now());

        mockOrder = new Order(46L);
        mockOrder.setCustomer(mockCustomer);
        mockOrder.setStatus("Pending");
        mockOrder.setTotal(BigDecimal.valueOf(100));
        mockOrder.setOrderDate(Instant.now());
    }

    @Test
    public void testCreateOrder() throws Exception {
        when(customerService.findById(33L)).thenReturn(mockCustomer);
        when(orderService.createOrder(any(Order.class))).thenReturn(mockOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":33,\"status\":\"Pending\",\"total\":100,\"orderDate\":null}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(46L))
                .andExpect(jsonPath("$.status").value("Pending"));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderService).createOrder(orderCaptor.capture());

        assertThat(orderCaptor.getValue().getCustomer().getId()).isEqualTo(33L);
    }

/*    @Test
    public void testGetOrdersByCustomer() throws Exception {
        when(customerService.findById(33L)).thenReturn(mockCustomer);
        when(orderService.findAllOrdersBasedOnCustomer(46L)).thenReturn(Collections.singletonList(mockOrder));

        mockMvc.perform(get("/api/orders/customer/{customerId}", 33L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(46L))
                .andExpect(jsonPath("$[0].status").value("Pending"))
                .andExpect(jsonPath("$[0].total").value(100.0));
    }*/

    @Test
    public void testGetOrderById() throws Exception {
        when(orderService.findOrderById(46L)).thenReturn(Optional.of(mockOrder));

        mockMvc.perform(get("/api/orders/{orderId}", 46L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(46));
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        when(orderService.updateOrderStatus(anyLong(), anyString())).thenReturn(mockOrder);

        mockMvc.perform(patch("/api/orders/status/{orderId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"Shipped\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pending")); // Check if it returns the updated order correctly
    }

    @Test
    public void testDeleteOrderById() throws Exception {
        doNothing().when(orderService).deleteOrderById(anyLong());

        mockMvc.perform(delete("/api/orders/{orderId}", 1L))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrderById(1L);
    }
}
