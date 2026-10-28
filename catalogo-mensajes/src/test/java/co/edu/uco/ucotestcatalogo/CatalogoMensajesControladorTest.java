package co.edu.uco.ucotestcatalogo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogoMensajesControladorTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void entregaUnMensajePorSuCodigo() throws Exception {
		mvc.perform(get("/api/v1/mensajes/GRUPO_NO_EXISTE"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.codigo").value("GRUPO_NO_EXISTE"))
				.andExpect(jsonPath("$.contenido").value("El grupo con ID {0} no existe"));
	}

	@Test
	void respondeNoEncontradoSiElCodigoNoExiste() throws Exception {
		mvc.perform(get("/api/v1/mensajes/NO_EXISTE")).andExpect(status().isNotFound());
	}

	@Test
	void entregaTodosLosMensajesConTildes() throws Exception {
		mvc.perform(get("/api/v1/mensajes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.SESION_INICIADA").value("Sesión iniciada"));
	}

}
