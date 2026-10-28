package co.edu.uco.ucotestapi.inicializador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = UcoTestApiApplication.PAQUETE_BASE)
@EnableJpaRepositories(basePackages = UcoTestApiApplication.PAQUETE_BASE)
@EntityScan(basePackages = UcoTestApiApplication.PAQUETE_BASE)
public class UcoTestApiApplication {
	static final String PAQUETE_BASE = "co.edu.uco.ucotestapi";

	public static void main(String[] args) {
		SpringApplication.run(UcoTestApiApplication.class, args);
	}

}
