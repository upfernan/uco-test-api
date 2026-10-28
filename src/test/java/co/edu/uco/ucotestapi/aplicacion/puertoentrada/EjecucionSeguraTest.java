package co.edu.uco.ucotestapi.aplicacion.puertoentrada;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.soporte.CatalogoMensajesPrueba;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

class EjecucionSeguraTest {
	private static final CodigoMensaje USUARIO = CodigoMensaje.REGISTRAR_NOTA_ERROR_INESPERADO_USUARIO;
	private static final CodigoMensaje TECNICO = CodigoMensaje.REGISTRAR_NOTA_ERROR_INESPERADO_TECNICO;

	@BeforeAll
	static void prepararCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	@Test
	void devuelveElResultadoDelTrabajo() {
		assertEquals("listo", EjecucionSegura.ejecutar(() -> "listo", USUARIO, TECNICO));
	}

	@Test
	void dejaPasarLasExcepcionesControladas() {
		var controlada = UcoTestApiExcepcion.crear("Mensaje para el usuario");

		var error = assertThrows(UcoTestApiExcepcion.class, () -> EjecucionSegura.ejecutar(() -> {
			throw controlada;
		}, USUARIO, TECNICO));

		assertSame(controlada, error);
	}

	@Test
	void envuelveLosErroresInesperadosConLosMensajesDelCatalogo() {
		var inesperado = new IllegalStateException("conexión perdida");

		var error = assertThrows(UcoTestApiExcepcion.class, () -> EjecucionSegura.ejecutar(() -> {
			throw inesperado;
		}, USUARIO, TECNICO));

		assertSame(inesperado, error.getExcepcionRaiz());
		assertFalse(error.getMensajeUsuario().contains("conexión perdida"));
		assertTrue(error.getMensajeTecnico().contains("IllegalStateException"));
	}

}