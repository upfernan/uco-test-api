package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.brevo;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertosalida.CodigoRecuperacionDTO;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionDTO;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.NotificadorCorreo;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Envia los correos (aviso de nota y codigo de recuperacion) con la API transaccional de Brevo.
 * Si se configura un correo para redirigir, todos los avisos llegan ahi (sirve para las demostraciones,
 * porque los correos demo terminan en .test y no existen).
 * Una falla al enviar nunca debe tumbar el registro de la nota, por eso no propaga excepciones.
 */
public class NotificacionCorreoBrevoAdaptador implements NotificadorCorreo {
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
		enviarCorreo(notificacion.correoDestinatario(), notificacion.nombreDestinatario(), notificacion.asunto(),
				notificacion.contenido());
	}

	@Override
	public void enviar(final CodigoRecuperacionDTO codigo) {
		enviarCorreo(codigo.correoDestinatario(), codigo.correoDestinatario(), codigo.asunto(), codigo.contenido());
	}

	private void enviarCorreo(final String correo, final String nombre, final String asunto, final String contenido) {
		var para = UtilTexto.tieneContenido(redirigirA) ? redirigirA : UtilTexto.aplicarTrim(correo);
		if (!UtilTexto.tieneContenido(para)) {
			LOGGER.warn("No se envió el correo \"{}\": el destinatario no tiene correo", asunto);
			return;
		}
		try {
			cliente.post()
					.uri(url)
					.header("api-key", claveApi)
					.contentType(MediaType.APPLICATION_JSON)
					.body(cuerpo(para, UtilTexto.obtenerValorDefecto(nombre, para), asunto, contenido))
					.retrieve()
					.toBodilessEntity();
			LOGGER.info("Correo enviado: {}", asunto);
		} catch (RuntimeException excepcion) {
			LOGGER.error("No se pudo enviar el correo \"{}\": {}", asunto, excepcion.getMessage());
		}
	}

	private Map<String, Object> cuerpo(final String para, final String nombre, final String asunto, final String contenido) {
		return Map.of(
				"sender", Map.of("name", nombreRemitente, "email", correoRemitente),
				"to", List.of(Map.of("email", para, "name", nombre)),
				"subject", asunto,
				"textContent", contenido);
	}

	public record PropiedadesBrevo(String url, String claveApi, String correoRemitente, String nombreRemitente,
			String redirigirA) {
	}

}
