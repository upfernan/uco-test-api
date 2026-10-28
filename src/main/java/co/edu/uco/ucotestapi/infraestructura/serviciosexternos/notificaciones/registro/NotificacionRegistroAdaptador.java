package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.registro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionDTO;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionPuertoSalida;

/**
 * Deja la notificacion en el log. Se usa en local y en las pruebas, cuando no hay clave de Brevo.
 * Una falla al notificar nunca debe tumbar el registro de la nota, por eso no propaga excepciones.
 */
public class NotificacionRegistroAdaptador implements NotificacionPuertoSalida {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionRegistroAdaptador.class);

	@Override
	public void notificar(final NotificacionDTO notificacion) {
		try {
			LOGGER.info("Notificación para {} | {} | {}", notificacion.nombreDestinatario(), notificacion.asunto(),
					notificacion.contenido());
		} catch (RuntimeException excepcion) {
			LOGGER.error("No se pudo enviar la notificación", excepcion);
		}
	}

}