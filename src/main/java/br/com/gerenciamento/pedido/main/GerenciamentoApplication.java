package br.com.gerenciamento.pedido.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"br.com.gerenciamento.pedido.config"}) // Especifica o pacote
public class GerenciamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerenciamentoApplication.class, args);
	}

}
