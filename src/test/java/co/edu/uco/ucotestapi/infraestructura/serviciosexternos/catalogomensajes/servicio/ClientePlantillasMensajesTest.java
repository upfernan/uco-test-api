package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class ClientePlantillasMensajesTest {
	private static final String URL = "https://catalogo.prueba";

	private final RestClient.Builder constructor = RestClient.builder();
	private final MockRestServiceServer catalogo = MockRestServiceServer.bindTo(constructor).build();
	private final ClientePlantillasMensajes cliente = new ClientePlantillasMensajes(constructor, URL);

	@Test
	void traeLaPlantillaDelServicio() {
		catalogo.expect(requestTo(URL + "/api/v1/mensajes/GRUPO_NO_EXISTE"))
				.andRespond(withSuccess("{\"codigo\":\"GRUPO_NO_EXISTE\",\"contenido\":\"El grupo con ID {0} no existe\"}",
						MediaType.APPLICATION_JSON));

		assertEquals("El grupo con ID {0} no existe", cliente.consultarPlantilla("GRUPO_NO_EXISTE"));
		catalogo.verify();
	}

	@Test
	void devuelveNullSiElCodigoNoExiste() {
		catalogo.expect(requestTo(URL + "/api/v1/mensajes/NO_EXISTE")).andRespond(withResourceNotFound());

		assertNull(cliente.consultarPlantilla("NO_EXISTE"));
		catalogo.verify();
	}

}
