package br.com.gerenciamento.pedido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gerenciamento.pedido.entity.Order;
import br.com.gerenciamento.pedido.exception.DuplicateOrderException;
import br.com.gerenciamento.pedido.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Recebe um externalId e uma lista de productIds para criar um pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "409", description = "Pedido com externalId já existe")
    })
    public ResponseEntity<?> createOrder(
            @RequestParam String externalId, // Recebe o externalId como parâmetro da requisição
            @RequestBody List<Long> productIds // Recebe a lista de productIds no corpo da requisição
    ) {
        try {
            Order order = orderService.createOrder(externalId, productIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(order); // Retorna o pedido criado com status 201
        } catch (DuplicateOrderException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Retorna erro 409 em caso de duplicação
        }
    }

    @GetMapping
    @Operation(summary = "Lista todos os pedidos", description = "Retorna uma lista de todos os pedidos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders); // Retorna a lista de pedidos com status 200
    }
}