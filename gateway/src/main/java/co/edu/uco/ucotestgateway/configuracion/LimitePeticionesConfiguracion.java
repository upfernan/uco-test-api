package co.edu.uco.ucotestgateway.configuracion;

import java.net.InetSocketAddress;
import java.util.Optional;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

/**
 * Llave del limite de peticiones: la IP de quien llama. Render pone la IP real en X-Forwarded-For,
 * asi que se toma la primera de esa lista y, si no viene, la direccion de la conexion.
 */
@Configuration
public class LimitePeticionesConfiguracion {
	private static final String IP_DESCONOCIDA = "desconocida";

	@Bean
	public KeyResolver llavePorIp() {
		return intercambio -> {
			var reenviada = intercambio.getRequest().getHeaders().getFirst("X-Forwarded-For");
			if (reenviada != null && !reenviada.isBlank()) {
				return Mono.just(reenviada.split(",")[0].trim());
			}
			var ip = Optional.ofNullable(intercambio.getRequest().getRemoteAddress())
					.map(InetSocketAddress::getAddress)
					.map(direccion -> direccion.getHostAddress())
					.orElse(IP_DESCONOCIDA);
			return Mono.just(ip);
		};
	}

}
