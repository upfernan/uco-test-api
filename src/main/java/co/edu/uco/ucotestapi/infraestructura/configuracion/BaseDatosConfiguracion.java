package co.edu.uco.ucotestapi.infraestructura.configuracion;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * En la nube la base llega como DATABASE_URL (postgresql://usuario:clave@host:puerto/base?sslmode=require).
 * JDBC no entiende ese formato, asi que aqui se convierte. En local no existe y se usa application.properties.
 */
@Configuration
@ConditionalOnProperty(name = "DATABASE_URL")
public class BaseDatosConfiguracion {
	private static final int PUERTO_POSTGRES = 5432;
	private static final String PARAMETRO_NO_SOPORTADO_POR_JDBC = "channel_binding";

	@Bean
	public DataSource dataSource(@Value("${DATABASE_URL}") final String urlBaseDatos) {
		var uri = URI.create(urlBaseDatos);
		var credenciales = uri.getUserInfo().split(":", 2);
		var puerto = uri.getPort() == -1 ? PUERTO_POSTGRES : uri.getPort();
		return DataSourceBuilder.create()
				.url("jdbc:postgresql://" + uri.getHost() + ":" + puerto + uri.getPath() + parametros(uri))
				.username(decodificar(credenciales[0]))
				.password(decodificar(credenciales[1]))
				.build();
	}

	private String parametros(final URI uri) {
		if (uri.getRawQuery() == null) {
			return "";
		}
		var parametros = Arrays.stream(uri.getRawQuery().split("&"))
				.filter(parametro -> !parametro.startsWith(PARAMETRO_NO_SOPORTADO_POR_JDBC + "="))
				.collect(Collectors.joining("&"));
		return parametros.isEmpty() ? "" : "?" + parametros;
	}

	private String decodificar(final String valor) {
		return URLDecoder.decode(valor, StandardCharsets.UTF_8);
	}

}