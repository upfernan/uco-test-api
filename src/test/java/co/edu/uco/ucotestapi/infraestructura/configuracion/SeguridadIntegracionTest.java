package co.edu.uco.ucotestapi.infraestructura.configuracion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Prueba las reglas de acceso con la aplicacion completa. Usa correos aleatorios para poder repetirse.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SeguridadIntegracionTest {

	private static final String GRUPO_DEMO = "33333333-3333-3333-3333-111111111111";
	private static final String CLAVE = "Prueba" + 2026;

	@Autowired
	private MockMvc mvc;

	@Test
	void laSaludEsPublica() throws Exception {
		mvc.perform(get("/actuator/health")).andExpect(status().isOk());
	}

	@Test
	void laRaizSinPaginaResponde404YNo500() throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.mensaje").value("El recurso solicitado no existe"));
	}

	@Test
	void unMetodoNoSoportadoResponde405() throws Exception {
		mvc.perform(get("/api/v1/auth/login"))
				.andExpect(status().isMethodNotAllowed())
				.andExpect(jsonPath("$.mensaje").value("La operación no está permitida para este recurso"));
	}

	@Test
	void sinTokenResponde401ConMensajeDelCatalogo() throws Exception {
		mvc.perform(get("/api/v1/grupos/" + GRUPO_DEMO + "/notas"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.mensaje").value("Debe iniciar sesión para usar este recurso"));
	}

	@Test
	void conTokenInvalidoResponde401() throws Exception {
		mvc.perform(get("/api/v1/grupos/" + GRUPO_DEMO + "/notas").header("Authorization", "Bearer abc.def.ghi"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void unEstudiantePuedeConsultarPeroNoRegistrarNotas() throws Exception {
		var token = registrarEIniciarSesion("ESTUDIANTE");

		mvc.perform(get("/api/v1/grupos/" + GRUPO_DEMO + "/notas").header("Authorization", "Bearer " + token))
				.andExpect(status().isOk());
		mvc.perform(post("/api/v1/notas-estudiantes").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.mensaje").value("No tiene permiso para realizar esta acción"));
	}

	@Test
	void unDocenteSinGrupoNoPuedeRegistrarNotaEnUnGrupoAjeno() throws Exception {
		var token = registrarEIniciarSesion("DOCENTE");

		mvc.perform(post("/api/v1/notas-estudiantes").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"idGrupo\":\"" + GRUPO_DEMO + "\",\"idEstudiante\":\"22222222-2222-2222-2222-111111111111\","
						+ "\"idNota\":\"55555555-5555-5555-5555-111111111111\",\"valor\":4}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value(org.hamcrest.Matchers.containsString("no está asignado al grupo")));
	}

	private String registrarEIniciarSesion(String rol) throws Exception {
		var correo = "prueba-" + UUID.randomUUID() + "@uco-test-api.test";
		mvc.perform(post("/api/v1/auth/registro").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nombre\":\"Persona Prueba\",\"correo\":\"" + correo + "\",\"clave\":\"" + CLAVE + "\",\"rol\":\"" + rol + "\"}"))
				.andExpect(status().isCreated());
		var respuesta = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"" + correo + "\",\"clave\":\"" + CLAVE + "\"}"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		return respuesta.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
	}

}