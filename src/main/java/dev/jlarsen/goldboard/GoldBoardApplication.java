package dev.jlarsen.goldboard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;

@SpringBootApplication
public class GoldBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldBoardApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadInitialData() {
		return (args) -> {

		};
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
