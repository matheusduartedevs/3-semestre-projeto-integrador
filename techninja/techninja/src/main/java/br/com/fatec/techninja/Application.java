package br.com.fatec.techninja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.fatec.techninja.model.Modulo;


// Classe de inicialização da aplicação Spring Boot, criando a classe Application que serve como ponto de entrada para a aplicação Spring Boot.

@SpringBootApplication
public class Application {

	// Método principal que inicia a execução da aplicação
	public static void main(String[] args) {

		// Executa o método estático run() de SpringApplication
		// Passando a classe Application e os argumentos fornecidos via linha de comando
		SpringApplication.run(Application.class, args);

	}

}

