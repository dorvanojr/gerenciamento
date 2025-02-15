package br.com.gerenciamento.pedido.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.gerenciamento.pedido.entity.Order;
import br.com.gerenciamento.pedido.exception.DuplicateOrderException;
import br.com.gerenciamento.pedido.service.OrderService;

class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setExternalId("123");
        order.setTotalAmount(new BigDecimal("45.00"));
        order.setStatus("CREATED");

        when(orderService.createOrder(any(), any())).thenReturn(order);

        mockMvc.perform(post("/orders?externalId=123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2, 3]"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.externalId").value("123"))
                .andExpect(jsonPath("$.totalAmount").value(45.00));
    }

    @Test
    void testCreateOrderWithDuplicateExternalId() throws Exception {
        when(orderService.createOrder(any(), any()))
            .thenThrow(new DuplicateOrderException("Pedido com ID externo já existe: 123"));

        mockMvc.perform(post("/orders?externalId=123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[1, 2, 3]"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Pedido com ID externo já existe: 123"));
    }
}
