package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.notificaciones;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.brevo.NotificacionCorreoBrevoAdaptador;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.registro.NotificacionRegistroAdaptador;

class NotificacionConfiguracionTest {
	private static final String URL = "https://brevo.prueba/v3/smtp/email";

	private final NotificacionConfiguracion configuracion = new NotificacionConfiguracion();

	@Test
	void conClaveYRemitenteUsaBrevo() {
		assertInstanceOf(NotificacionCorreoBrevoAdaptador.class,
				configuracion.notificacionPuertoSalida(URL, "clave", "remitente@correo.prueba", "UCO Test API", ""));
	}

	@Test
	void sinClaveOSinRemitenteUsaElLog() {
		assertInstanceOf(NotificacionRegistroAdaptador.class,
				configuracion.notificacionPuertoSalida(URL, "", "remitente@correo.prueba", "UCO Test API", ""));
		assertInstanceOf(NotificacionRegistroAdaptador.class,
				configuracion.notificacionPuertoSalida(URL, "clave", " ", "UCO Test API", ""));
	}

}
