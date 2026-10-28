package co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteGrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.GrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaEstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.soporte.CatalogoMensajesPrueba;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

/**
 * Reglas de los objetos de dominio que no necesitan consultar la base: identificadores nulos y valor de la nota.
 * Las reglas que consultan repositorios se prueban en RegistrarNotaEstudianteCasoUsoImplTest.
 */
class ObjetosDominioRegistrarNotaTest {
	private static final UUID ID = UUID.randomUUID();

	@BeforeAll
	static void prepararCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	private static void assertMensaje(final CodigoMensaje codigo, final Executable accion) {
		assertEquals(CatalogoMensajesProveedor.contenido(codigo),
				assertThrows(UcoTestApiExcepcion.class, accion).getMensajeUsuario());
	}

	@Test
	void escalaLaNotaADosDecimales() {
		assertEquals(350, NotaEstudianteDominio.escalarValor(new BigDecimal("3.5")));
		assertEquals(500, NotaEstudianteDominio.escalarValor(new BigDecimal("5.00")));
		assertEquals(0, NotaEstudianteDominio.escalarValor(BigDecimal.ZERO));
	}

	@Test
	void rechazaValorNuloOConMasDeDosDecimales() {
		assertMensaje(CodigoMensaje.VALOR_NOTA_NULO, () -> NotaEstudianteDominio.escalarValor(null));
		assertMensaje(CodigoMensaje.VALOR_NOTA_MAXIMO_DOS_DECIMALES,
				() -> NotaEstudianteDominio.escalarValor(new BigDecimal("3.555")));
	}

	@ParameterizedTest
	@CsvSource({ "501", "-50" })
	void rechazaValoresFueraDeRango(final int valorEscalado) {
		var nota = new NotaEstudianteDominio(ID, ID, valorEscalado, mock(NotaEstudianteRepositorio.class));
		assertMensaje(CodigoMensaje.VALOR_NOTA_FUERA_DE_RANGO, nota::validarValorEnRango);
	}

	@Test
	void cadaObjetoRechazaSuIdentificadorNulo() {
		assertMensaje(CodigoMensaje.GRUPO_ID_NULO,
				() -> new GrupoDominio(null, mock(GrupoRepositorio.class)).validarGrupoExista());
		assertMensaje(CodigoMensaje.DOCENTE_ID_NULO,
				() -> new DocenteDominio(null, mock(DocenteRepositorio.class)).validarDocenteExista());
		assertMensaje(CodigoMensaje.ESTUDIANTE_ID_NULO,
				() -> new EstudianteDominio(null, mock(EstudianteRepositorio.class)).validarEstudianteExista());
		assertMensaje(CodigoMensaje.ESTUDIANTE_GRUPO_IDS_NULOS, () -> new EstudianteGrupoDominio(ID, null,
				mock(EstudianteGrupoRepositorio.class)).validarEstudianteExistaEnGrupo());
		assertMensaje(CodigoMensaje.NOTA_ID_NULO,
				() -> new NotaDominio(null, mock(NotaRepositorio.class)).validarNotaExista());
		assertMensaje(CodigoMensaje.NOTA_ESTUDIANTE_IDS_NULOS, () -> new NotaEstudianteDominio(null, ID, 350,
				mock(NotaEstudianteRepositorio.class)).validarNotaNoRegistrada());
	}

	@Test
	void exigeLosRepositorios() {
		assertThrows(NullPointerException.class, () -> new GrupoDominio(ID, null));
	}

}