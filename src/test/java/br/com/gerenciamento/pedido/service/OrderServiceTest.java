package br.com.gerenciamento.pedido.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.gerenciamento.pedido.entity.Order;
import br.com.gerenciamento.pedido.entity.Product;
import br.com.gerenciamento.pedido.exception.DuplicateOrderException;
import br.com.gerenciamento.pedido.repository.OrderRepository;
import br.com.gerenciamento.pedido.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderWithDuplicateExternalId() {
        // Arrange (Preparação)
        when(orderRepository.findByExternalId("123")).thenReturn(Optional.of(new Order()));

        // Act & Assert (Execução e Verificação)
        assertThrows(DuplicateOrderException.class, () -> {
            orderService.createOrder("123", Arrays.asList(1L, 2L));
        });

        // Verifica se o método save NÃO foi chamado
        verify(orderRepository, never()).save(any(Order.class));
    }
    
    @Test
    void testGetAllOrders() {
        // Arrange (Preparação)
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTotalAmount(new BigDecimal("30.00"));

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTotalAmount(new BigDecimal("50.00"));

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        // Act (Execução)
        List<Order> orders = orderService.getAllOrders();

        // Assert (Verificação)
        assertEquals(2, orders.size()); // Verifica a quantidade de pedidos
        assertEquals(new BigDecimal("30.00"), orders.get(0).getTotalAmount()); // Verifica o valor do primeiro pedido
        assertEquals(new BigDecimal("50.00"), orders.get(1).getTotalAmount()); // Verifica o valor do segundo pedido

        // Verifica se o método findAll foi chamado
        verify(orderRepository, times(1)).findAll();
    }
}