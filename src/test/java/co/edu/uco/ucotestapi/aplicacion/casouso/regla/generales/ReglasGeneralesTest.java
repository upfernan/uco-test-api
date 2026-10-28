package co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import co.edu.uco.ucotestapi.aplicacion.casouso.ParametrosEjecucion;
import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.TextoPresenteEspecificacion;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.soporte.CatalogoMensajesPrueba;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

class ReglasGeneralesTest {

	@BeforeAll
	static void prepararCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	@Test
	void cumpleEspecificacionLanzaElMensajeDelCatalogoConSusParametros() {
		var id = UUID.randomUUID();
		var especificacion = new TextoPresenteEspecificacion();

		var error = assertThrows(UcoTestApiExcepcion.class,
				() -> CumpleEspecificacionRegla.ejecutarRegla("", especificacion, CodigoMensaje.DOCENTE_NO_ACTIVO, id));

		assertEquals("El docente con ID " + id + " no está activo", error.getMensajeUsuario());
		assertTrue(error.getMensajeTecnico().contains("DOCENTE_NO_ACTIVO"));
	}

	@Test
	void cumpleEspecificacionNoHaceNadaSiSeCumple() {
		var especificacion = new TextoPresenteEspecificacion();
		assertDoesNotThrow(() -> CumpleEspecificacionRegla.ejecutarRegla("texto", especificacion, CodigoMensaje.GRUPO_ID_NULO));
	}

	@Test
	void uuidPresenteRechazaElValorPorDefecto() {
		var error = assertThrows(UcoTestApiExcepcion.class,
				() -> UUIDPresenteRegla.ejecutarRegla(UtilUUID.UUID_DEFECTO, CodigoMensaje.GRUPO_ID_NULO));

		assertEquals("El ID del grupo no puede ser nulo", error.getMensajeUsuario());
	}

	@Test
	void existePorIdLanzaElCodigoIndicadoSiNoExiste() {
		var repositorio = mock(DocenteRepositorio.class);
		var id = UUID.randomUUID();
		when(repositorio.existePorId(id)).thenReturn(false);

		var error = assertThrows(UcoTestApiExcepcion.class,
				() -> ExistePorIdRegla.ejecutarRegla(id, repositorio, CodigoMensaje.DOCENTE_NO_EXISTE));

		assertEquals("El docente con ID " + id + " no existe", error.getMensajeUsuario());
	}

	@Test
	void existePorIdPasaSiExiste() {
		var repositorio = mock(DocenteRepositorio.class);
		var id = UUID.randomUUID();
		when(repositorio.existePorId(id)).thenReturn(true);

		assertDoesNotThrow(() -> ExistePorIdRegla.ejecutarRegla(id, repositorio, CodigoMensaje.DOCENTE_NO_EXISTE));
	}

	@Test
	void parametrosIncompletosDanUnErrorTecnicoConElNombreDeQuienLlama() {
		Object[] datos = { "texto" };

		var error = assertThrows(UcoTestApiExcepcion.class,
				() -> ParametrosEjecucion.obtener(datos, 1, UUID.class, ReglasGeneralesTest.class));

		assertTrue(error.getMensajeTecnico().contains("ReglasGeneralesTest"));
	}

}