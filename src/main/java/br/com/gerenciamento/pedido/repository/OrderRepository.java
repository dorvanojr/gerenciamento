package br.com.gerenciamento.pedido.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gerenciamento.pedido.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	 Optional<Order> findByExternalId(String externalId);
}
