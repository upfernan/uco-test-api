package co.edu.uco.ucotestapi.infraestructura.persistencia.configuracion;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * En la nube la base llega como DATABASE_URL (ver UrlBaseDatos). En local no existe y se usa application.properties.
 */
@Configuration
@ConditionalOnProperty(name = "DATABASE_URL")
public class BaseDatosConfiguracion {

	@Bean
	public DataSource dataSource(@Value("${DATABASE_URL}") final String valorUrlBaseDatos) {
		var urlBaseDatos = UrlBaseDatos.desde(valorUrlBaseDatos);
		var constructor = DataSourceBuilder.create().url(urlBaseDatos.jdbcUrl());
		if (urlBaseDatos.tieneCredenciales()) {
			constructor.username(urlBaseDatos.usuario()).password(urlBaseDatos.clave());
		}
		return constructor.build();
	}

}
