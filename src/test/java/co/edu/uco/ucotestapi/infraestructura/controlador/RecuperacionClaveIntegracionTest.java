package co.edu.uco.ucotestapi.infraestructura.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.inicializador.UcoTestApiApplication;

/**
 * Recuperar la contrasena de punta a punta contra la base. Como el codigo real solo viaja por correo,
 * la prueba reemplaza su version cifrada por la de un codigo conocido.
 */
@SpringBootTest(classes = UcoTestApiApplication.class)
@AutoConfigureMockMvc
class RecuperacionClaveIntegracionTest {
	private static final String CLAVE = "Prueba" + 2026;
	private static final String CLAVE_NUEVA = "Cambiada" + 2026;
	private static final String CODIGO = "482915";

	@Autowired
	private MockMvc mvc;
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private ProveedorIdentidadPuertoSalida proveedorIdentidad;

	@Test
	void recuperaLaContrasenaConElCodigoYLaUsaParaEntrar() throws Exception {
		var correo = "recupera-" + UUID.randomUUID() + "@uco-test-api.test";
		mvc.perform(post("/api/v1/auth/registro").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nombre\":\"Persona Prueba\",\"correo\":\"" + correo + "\",\"clave\":\"" + CLAVE + "\",\"rol\":\"ESTUDIANTE\"}"))
				.andExpect(status().isCreated());

		enviar("/api/v1/auth/recuperar-clave", "{\"correo\":\"" + correo + "\"}")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.mensajes[0]").value(org.hamcrest.Matchers.containsString("te enviamos un código")));
		jdbc.update("UPDATE recuperacion_clave SET codigo_hash = ? WHERE id_usuario = (SELECT id FROM usuario WHERE correo = ?)",
				proveedorIdentidad.cifrarClave(CODIGO), correo);

		restablecer(correo, "000000").andExpect(status().isBadRequest());
		assertEquals(1, jdbc.queryForObject("SELECT intentos FROM recuperacion_clave WHERE id_usuario = "
				+ "(SELECT id FROM usuario WHERE correo = ?)", Integer.class, correo));

		restablecer(correo, CODIGO)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.mensajes[0]").value("Tu contraseña se cambió correctamente. Ya puedes iniciar sesión."));
		restablecer(correo, CODIGO).andExpect(status().isBadRequest());

		enviar("/api/v1/auth/login", "{\"correo\":\"" + correo + "\",\"clave\":\"" + CLAVE_NUEVA + "\"}").andExpect(status().isOk());
		enviar("/api/v1/auth/login", "{\"correo\":\"" + correo + "\",\"clave\":\"" + CLAVE + "\"}").andExpect(status().isBadRequest());
	}

	@Test
	void unCorreoSinCuentaRecibeLaMismaRespuesta() throws Exception {
		enviar("/api/v1/auth/recuperar-clave", "{\"correo\":\"nadie-" + UUID.randomUUID() + "@uco-test-api.test\"}")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.respuestaExitosa").value(true));
	}

	private ResultActions restablecer(final String correo, final String codigo) throws Exception {
		return enviar("/api/v1/auth/restablecer-clave",
				"{\"correo\":\"" + correo + "\",\"codigo\":\"" + codigo + "\",\"claveNueva\":\"" + CLAVE_NUEVA + "\"}");
	}

	private ResultActions enviar(final String ruta, final String cuerpo) throws Exception {
		return mvc.perform(post(ruta).contentType(MediaType.APPLICATION_JSON).content(cuerpo));
	}

}
