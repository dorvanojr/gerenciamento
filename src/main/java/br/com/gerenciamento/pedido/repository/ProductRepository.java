package br.com.gerenciamento.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gerenciamento.pedido.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}