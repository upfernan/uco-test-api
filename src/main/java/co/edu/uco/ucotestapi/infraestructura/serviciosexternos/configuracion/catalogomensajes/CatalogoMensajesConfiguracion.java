package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.catalogomensajes;

import java.time.Clock;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.CatalogoMensajesPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.propiedades.adaptador.CatalogoMensajesPropiedadesAdaptador;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio.ClientePlantillasMensajes;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio.adaptador.CatalogoMensajesServicioAdaptador;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Elige el adaptador del catalogo de mensajes: el servicio externo si hay URL, o el archivo de la API si no
 * (local y pruebas). El archivo queda siempre como respaldo. Los casos de uso no se enteran de cual reciben.
 */
@Configuration
public class CatalogoMensajesConfiguracion {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogoMensajesConfiguracion.class);
	private static final Duration TIEMPO_ESPERA = Duration.ofSeconds(2);

	@Bean
	public ClientePlantillasMensajes clientePlantillasMensajes(@Value("${catalogo.mensajes.url:}") final String url) {
		var fabrica = new SimpleClientHttpRequestFactory();
		fabrica.setConnectTimeout(TIEMPO_ESPERA);
		fabrica.setReadTimeout(TIEMPO_ESPERA);
		return new ClientePlantillasMensajes(RestClient.builder().requestFactory(fabrica), UtilTexto.aplicarTrim(url));
	}

	@Bean
	public CatalogoMensajesPuertoSalida catalogoMensajesPuertoSalida(final MessageSource messageSource,
			final ClientePlantillasMensajes cliente, @Value("${catalogo.mensajes.url:}") final String url) {
		var archivo = new CatalogoMensajesPropiedadesAdaptador(messageSource);
		if (!UtilTexto.tieneContenido(url)) {
			LOGGER.info("Catálogo de mensajes desde el archivo de la API: falta CATALOGO_MENSAJES_URL");
			return archivo;
		}
		LOGGER.info("Catálogo de mensajes desde el servicio externo");
		return new CatalogoMensajesServicioAdaptador(cliente, archivo, Clock.systemUTC());
	}

}
