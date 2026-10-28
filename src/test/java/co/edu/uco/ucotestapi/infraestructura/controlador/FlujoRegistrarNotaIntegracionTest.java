package co.edu.uco.ucotestapi.infraestructura.controlador;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

import co.edu.uco.ucotestapi.inicializador.UcoTestApiApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Recorre el flujo completo contra la base de datos: cuentas, consultas y registro de notas. Crea sus propios datos
 * dentro de una transacción que se revierte al final, así puede repetirse sin ensuciar la base.
 */
@SpringBootTest(classes = UcoTestApiApplication.class)
@AutoConfigureMockMvc
@Transactional
class FlujoRegistrarNotaIntegracionTest {
	private static final String CLAVE = "Prueba" + 2026;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@PersistenceContext
	private EntityManager entityManager;

	private final UUID idDocente = UUID.randomUUID();
	private final UUID idEstudiante = UUID.randomUUID();
	private final UUID idGrupo = UUID.randomUUID();
	private final UUID idNotaAbierta = UUID.randomUUID();
	private final UUID idNotaCerrada = UUID.randomUUID();
	private final String sufijo = UUID.randomUUID().toString();
	private final String correoDocente = "docente-" + sufijo + "@uco-test-api.test";
	private final String correoEstudiante = "estudiante-" + sufijo + "@uco-test-api.test";

	@BeforeEach
	void prepararDatos() {
		jdbc.update("INSERT INTO docente (id, nombre, estado, correo) VALUES (?, 'Docente Flujo', 'ACTIVO', ?)", idDocente,
				correoDocente);
		jdbc.update("INSERT INTO estudiante (id, nombre, correo) VALUES (?, 'Estudiante Flujo', ?)", idEstudiante,
				correoEstudiante);
		jdbc.update("INSERT INTO grupo (id, nombre, id_docente, estado, fecha_inicio, fecha_fin) "
				+ "VALUES (?, 'Grupo Flujo', ?, 'ACTIVO', DATE '2026-01-01', DATE '2030-12-31')", idGrupo, idDocente);
		jdbc.update("INSERT INTO estudiante_grupo (id, id_estudiante, id_grupo) VALUES (?, ?, ?)", UUID.randomUUID(),
				idEstudiante, idGrupo);
		jdbc.update("INSERT INTO nota (id, nombre, id_grupo, fecha_registro_desde, fecha_registro_hasta, estado) "
				+ "VALUES (?, 'Parcial Flujo', ?, TIMESTAMPTZ '2026-01-01 00:00:00Z', TIMESTAMPTZ '2030-12-31 23:59:59Z', 'ACTIVO')",
				idNotaAbierta, idGrupo);
		jdbc.update("INSERT INTO nota (id, nombre, id_grupo, fecha_registro_desde, fecha_registro_hasta, estado) "
				+ "VALUES (?, 'Quiz Flujo', ?, TIMESTAMPTZ '2026-01-01 00:00:00Z', TIMESTAMPTZ '2026-01-02 00:00:00Z', 'ACTIVO')",
				idNotaCerrada, idGrupo);
	}

	@Test
	void elDocenteConsultaSusGruposYRegistraUnaNota() throws Exception {
		registrar("Docente Flujo", correoDocente, "DOCENTE")
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.datos.idPersona").value(idDocente.toString()));
		var token = iniciarSesion(correoDocente);

		conToken(get("/api/v1/docentes/" + idDocente + "/grupos"), token)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.datos[0].nombre").value("Grupo Flujo"));
		conToken(get("/api/v1/grupos/" + idGrupo + "/estudiantes"), token)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.datos[0].id").value(idEstudiante.toString()));
		conToken(get("/api/v1/grupos/" + idGrupo + "/notas"), token)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.datos.length()").value(2));

		registrarNota(token, idNotaAbierta, "3.5")
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.mensaje").value("La nota se registró correctamente"));
		entityManager.flush();
		assertEquals(350, jdbc.queryForObject("SELECT valor FROM nota_estudiante WHERE id_nota = ?", Integer.class,
				idNotaAbierta));

		registrarNota(token, idNotaAbierta, "4").andExpect(status().isBadRequest());
		registrarNota(token, idNotaCerrada, "4").andExpect(status().isBadRequest());
		registrarNota(token, idNotaAbierta, "5.123").andExpect(status().isBadRequest());
	}

	@Test
	void elEstudianteSeRegistraYConsultaLasNotasDelGrupo() throws Exception {
		registrar("Estudiante Flujo", correoEstudiante, "ESTUDIANTE")
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.datos.idPersona").value(idEstudiante.toString()));
		var token = iniciarSesion(correoEstudiante);

		conToken(get("/api/v1/grupos/" + idGrupo + "/notas"), token).andExpect(status().isOk());
	}

	@Test
	void noSePuedeRegistrarDosVecesElMismoCorreoNiEntrarConOtraClave() throws Exception {
		registrar("Docente Flujo", correoDocente, "DOCENTE").andExpect(status().isCreated());

		registrar("Docente Flujo", correoDocente, "DOCENTE")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mensaje").value(containsString("Ya existe una cuenta")));
		mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"" + correoDocente + "\",\"clave\":\"Otra2026\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void unCuerpoInvalidoResponde400() throws Exception {
		mvc.perform(post("/api/v1/auth/registro").contentType(MediaType.APPLICATION_JSON).content("{no es json"))
				.andExpect(status().isBadRequest());
	}

	private ResultActions registrar(final String nombre, final String correo, final String rol) throws Exception {
		return mvc.perform(post("/api/v1/auth/registro").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nombre\":\"" + nombre + "\",\"correo\":\"" + correo + "\",\"clave\":\"" + CLAVE
						+ "\",\"rol\":\"" + rol + "\"}"));
	}

	private String iniciarSesion(final String correo) throws Exception {
		var respuesta = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"correo\":\"" + correo + "\",\"clave\":\"" + CLAVE + "\"}"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		return JsonPath.read(respuesta, "$.datos.token");
	}

	private ResultActions registrarNota(final String token, final UUID idNota, final String valor) throws Exception {
		return conToken(post("/api/v1/notas-estudiantes").contentType(MediaType.APPLICATION_JSON)
				.content("{\"idGrupo\":\"" + idGrupo + "\",\"idEstudiante\":\"" + idEstudiante + "\",\"idNota\":\""
						+ idNota + "\",\"valor\":" + valor + "}"), token);
	}

	private ResultActions conToken(
			final org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder peticion,
			final String token) throws Exception {
		return mvc.perform(peticion.header("Authorization", "Bearer " + token));
	}

}
