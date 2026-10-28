package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.notificaciones;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.brevo.NotificacionCorreoBrevoAdaptador;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.brevo.NotificacionCorreoBrevoAdaptador.PropiedadesBrevo;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.registro.NotificacionRegistroAdaptador;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Elige el adaptador de notificaciones: correo con Brevo si el vault entrega la clave y el remitente,
 * o el log si no (local y pruebas). El caso de uso no se entera de cual recibe.
 */
@Configuration
public class NotificacionConfiguracion {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionConfiguracion.class);
	private static final Duration TIEMPO_CONEXION = Duration.ofSeconds(3);
	private static final Duration TIEMPO_LECTURA = Duration.ofSeconds(5);

	@Bean
	public NotificacionPuertoSalida notificacionPuertoSalida(
			@Value("${notificaciones.brevo.url}") final String url,
			@Value("${notificaciones.brevo.clave-api:}") final String claveApi,
			@Value("${notificaciones.correo.remitente:}") final String correoRemitente,
			@Value("${notificaciones.correo.nombre-remitente}") final String nombreRemitente,
			@Value("${notificaciones.correo.redirigir-a:}") final String redirigirA) {
		if (!UtilTexto.tieneContenido(claveApi) || !UtilTexto.tieneContenido(correoRemitente)) {
			LOGGER.info("Notificaciones por log: falta BREVO_API_KEY o CORREO_REMITENTE");
			return new NotificacionRegistroAdaptador();
		}
		LOGGER.info("Notificaciones por correo con Brevo");
		var fabrica = new SimpleClientHttpRequestFactory();
		fabrica.setConnectTimeout(TIEMPO_CONEXION);
		fabrica.setReadTimeout(TIEMPO_LECTURA);
		return new NotificacionCorreoBrevoAdaptador(RestClient.builder().requestFactory(fabrica),
				new PropiedadesBrevo(url, claveApi, correoRemitente, nombreRemitente, redirigirA));
	}

}
