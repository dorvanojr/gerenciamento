package br.com.gerenciamento.pedido.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BinaryOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gerenciamento.pedido.entity.Order;
import br.com.gerenciamento.pedido.entity.Product;
import br.com.gerenciamento.pedido.exception.DuplicateOrderException;
import br.com.gerenciamento.pedido.repository.OrderRepository;
import br.com.gerenciamento.pedido.repository.ProductRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(String externalId, List<Long> productIds) {
        // Verifica se já existe um pedido com o mesmo ID externo
        if (orderRepository.findByExternalId(externalId).isPresent()) {
            throw new DuplicateOrderException("Pedido com ID externo já existe: " + externalId);
        }

        // Busca os produtos no banco de dados com base nos IDs
        List<Product> products = productRepository.findAllById(productIds);

        // Calcula o valor total do pedido somando os preços dos produtos
        BigDecimal totalAmount = products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Cria o pedido com os produtos e o valor total
        Order order = new Order();
        order.setExternalId(externalId); // Define o ID externo
        order.setProducts(products);
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");

        // Salva o pedido no banco de dados
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
