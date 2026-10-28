package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.brevo;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionDTO;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionPuertoSalida;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Envia la notificacion por correo con la API transaccional de Brevo.
 * Si se configura un correo para redirigir, todos los avisos llegan ahi (sirve para las demostraciones,
 * porque los correos demo terminan en .test y no existen).
 * Una falla al enviar nunca debe tumbar el registro de la nota, por eso no propaga excepciones.
 */
public class NotificacionCorreoBrevoAdaptador implements NotificacionPuertoSalida {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionCorreoBrevoAdaptador.class);

	private final RestClient cliente;
	private final String url;
	private final String claveApi;
	private final String correoRemitente;
	private final String nombreRemitente;
	private final String redirigirA;

	public NotificacionCorreoBrevoAdaptador(final RestClient.Builder constructor, final PropiedadesBrevo propiedades) {
		this.cliente = constructor.build();
		this.url = propiedades.url();
		this.claveApi = propiedades.claveApi();
		this.correoRemitente = propiedades.correoRemitente();
		this.nombreRemitente = propiedades.nombreRemitente();
		this.redirigirA = UtilTexto.aplicarTrim(propiedades.redirigirA());
	}

	@Override
	public void notificar(final NotificacionDTO notificacion) {
		var para = UtilTexto.tieneContenido(redirigirA) ? redirigirA : UtilTexto.aplicarTrim(notificacion.correoDestinatario());
		if (!UtilTexto.tieneContenido(para)) {
			LOGGER.warn("No se envió el correo \"{}\": el destinatario no tiene correo", notificacion.asunto());
			return;
		}
		try {
			cliente.post()
					.uri(url)
					.header("api-key", claveApi)
					.contentType(MediaType.APPLICATION_JSON)
					.body(cuerpo(para, notificacion))
					.retrieve()
					.toBodilessEntity();
			LOGGER.info("Correo enviado: {}", notificacion.asunto());
		} catch (RuntimeException excepcion) {
			LOGGER.error("No se pudo enviar el correo \"{}\": {}", notificacion.asunto(), excepcion.getMessage());
		}
	}

	private Map<String, Object> cuerpo(final String para, final NotificacionDTO notificacion) {
		return Map.of(
				"sender", Map.of("name", nombreRemitente, "email", correoRemitente),
				"to", List.of(Map.of("email", para, "name", UtilTexto.obtenerValorDefecto(notificacion.nombreDestinatario(), para))),
				"subject", notificacion.asunto(),
				"textContent", notificacion.contenido());
	}

	public record PropiedadesBrevo(String url, String claveApi, String correoRemitente, String nombreRemitente,
			String redirigirA) {
	}

}
