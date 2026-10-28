package co.edu.uco.ucotestapi.transversal.respuesta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class RespuestaTest {

	@Test
	void exitosaConUnDatoLoEnvuelveEnLista() {
		var respuesta = Respuesta.crearExitosa("dato").agregarMensaje("  Listo  ");

		assertTrue(respuesta.isRespuestaExitosa());
		assertEquals(List.of("dato"), respuesta.getDatos());
		assertEquals(List.of("Listo"), respuesta.getMensajes());
	}

	@Test
	void exitosaConListaConservaLosDatos() {
		var respuesta = Respuesta.crearExitosa(List.of(1, 2));

		assertEquals(List.of(1, 2), respuesta.getDatos());
		assertTrue(respuesta.getMensajes().isEmpty());
	}

	@Test
	void fallidaNoTieneDatosEIgnoraMensajesVacios() {
		Respuesta<Void> respuesta = Respuesta.crearFallida("Error");
		respuesta.agregarMensaje("   ").agregarMensaje(null);

		assertFalse(respuesta.isRespuestaExitosa());
		assertTrue(respuesta.getDatos().isEmpty());
		assertEquals(List.of("Error"), respuesta.getMensajes());
	}

	@Test
	void listasNulasQuedanVacias() {
		var respuesta = new Respuesta<String>(null, null, true);

		assertTrue(respuesta.getMensajes().isEmpty());
		assertTrue(respuesta.getDatos().isEmpty());
	}

}
