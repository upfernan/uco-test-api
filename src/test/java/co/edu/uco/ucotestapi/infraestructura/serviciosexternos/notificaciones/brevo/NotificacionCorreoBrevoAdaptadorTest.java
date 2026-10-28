package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.brevo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionDTO;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones.brevo.NotificacionCorreoBrevoAdaptador.PropiedadesBrevo;

class NotificacionCorreoBrevoAdaptadorTest {
	private static final String URL = "https://brevo.prueba/v3/smtp/email";
	private static final NotificacionDTO AVISO = new NotificacionDTO("Estudiante Demo", "estudiante@correo.prueba",
			"Nueva nota registrada: Parcial 1", "Hola Estudiante Demo, se registró tu nota en Parcial 1: 3.50");

	private final RestClient.Builder constructor = RestClient.builder();
	private final MockRestServiceServer brevo = MockRestServiceServer.bindTo(constructor).build();

	private NotificacionCorreoBrevoAdaptador adaptador(final String redirigirA) {
		return new NotificacionCorreoBrevoAdaptador(constructor,
				new PropiedadesBrevo(URL, "clave-prueba", "remitente@correo.prueba", "UCO Test API", redirigirA));
	}

	@Test
	void enviaElCorreoAlEstudianteConLaClaveYElRemitente() {
		var notificacion = adaptador("");
		brevo.expect(requestTo(URL))
				.andExpect(method(HttpMethod.POST))
				.andExpect(header("api-key", "clave-prueba"))
				.andExpect(jsonPath("$.sender.email").value("remitente@correo.prueba"))
				.andExpect(jsonPath("$.to[0].email").value("estudiante@correo.prueba"))
				.andExpect(jsonPath("$.to[0].name").value("Estudiante Demo"))
				.andExpect(jsonPath("$.subject").value("Nueva nota registrada: Parcial 1"))
				.andExpect(jsonPath("$.textContent").value("Hola Estudiante Demo, se registró tu nota en Parcial 1: 3.50"))
				.andRespond(withSuccess());

		notificacion.notificar(AVISO);

		brevo.verify();
	}

	@Test
	void redirigeTodosLosCorreosCuandoEstaConfigurado() {
		var notificacion = adaptador(" demo@correo.prueba ");
		brevo.expect(requestTo(URL))
				.andExpect(jsonPath("$.to[0].email").value("demo@correo.prueba"))
				.andRespond(withSuccess());

		notificacion.notificar(AVISO);

		brevo.verify();
	}

	@Test
	void unaFallaDeBrevoNoTumbaElRegistro() {
		var notificacion = adaptador("");
		brevo.expect(requestTo(URL)).andRespond(withServerError());

		assertDoesNotThrow(() -> notificacion.notificar(AVISO));
		brevo.verify();
	}

	@Test
	void sinCorreoDelDestinatarioNoLlamaABrevo() {
		var notificacion = adaptador("");

		notificacion.notificar(new NotificacionDTO("Sin Correo", null, "Asunto", "Contenido"));

		brevo.verify();
	}

}
