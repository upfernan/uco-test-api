package co.edu.uco.ucotestapi.infraestructura.output.serviciosexternos.notificaciongateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.notificaciongateway.NotificacionDTO;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.notificaciongateway.NotificadorServicio;

/**
 * Adaptador provisional: deja la notificacion en el log mientras se conecta el proveedor de correo.
 * Una falla al notificar nunca debe tumbar el registro de la nota, por eso no propaga excepciones.
 */
@Component
public class NotificadorRegistroAdapter implements NotificadorServicio {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificadorRegistroAdapter.class);

	@Override
	public void notificar(final NotificacionDTO notificacion) {
		try {
			LOGGER.info("Notificación para {} | {} | {}", notificacion.destinatario(), notificacion.asunto(),
					notificacion.contenido());
		} catch (RuntimeException excepcion) {
			LOGGER.error("No se pudo enviar la notificación", excepcion);
		}
	}

}