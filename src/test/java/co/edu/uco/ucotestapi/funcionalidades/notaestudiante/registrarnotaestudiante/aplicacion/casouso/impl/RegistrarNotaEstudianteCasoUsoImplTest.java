package co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.dominio.RegistrarNotaEstudianteDominio;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.impl.mapeador.RegistrarNotaEstudianteMapeadorCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionDTO;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteGrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.GrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaEstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEstudianteEntidad;
import co.edu.uco.ucotestapi.soporte.CatalogoMensajesPrueba;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoDocente;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoGrupo;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoNota;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

class RegistrarNotaEstudianteCasoUsoImplTest {
	private static final UUID ID_DOCENTE = UUID.randomUUID();
	private static final UUID ID_GRUPO = UUID.randomUUID();
	private static final UUID ID_ESTUDIANTE = UUID.randomUUID();
	private static final UUID ID_NOTA = UUID.randomUUID();

	private final DocenteRepositorio docenteRepositorio = mock(DocenteRepositorio.class);
	private final EstudianteRepositorio estudianteRepositorio = mock(EstudianteRepositorio.class);
	private final GrupoRepositorio grupoRepositorio = mock(GrupoRepositorio.class);
	private final EstudianteGrupoRepositorio estudianteGrupoRepositorio = mock(EstudianteGrupoRepositorio.class);
	private final NotaRepositorio notaRepositorio = mock(NotaRepositorio.class);
	private final NotaEstudianteRepositorio notaEstudianteRepositorio = mock(NotaEstudianteRepositorio.class);
	private final NotificacionPuertoSalida notificacion = mock(NotificacionPuertoSalida.class);
	private final DocenteEntidad docente = new DocenteEntidad();
	private final GrupoEntidad grupo = new GrupoEntidad();
	private final NotaEntidad nota = new NotaEntidad();
	private RegistrarNotaEstudianteCasoUsoImpl casoUso;

	@BeforeAll
	static void prepararCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	@BeforeEach
	void preparar() {
		casoUso = new RegistrarNotaEstudianteCasoUsoImpl(docenteRepositorio, estudianteRepositorio, grupoRepositorio,
				estudianteGrupoRepositorio, notaRepositorio, notaEstudianteRepositorio, notificacion,
				new RegistrarNotaEstudianteMapeadorCasoUso());
		docente.setId(ID_DOCENTE);
		docente.setEstado(EstadoDocente.ACTIVO);
		grupo.setId(ID_GRUPO);
		grupo.setDocente(docente);
		grupo.setEstado(EstadoGrupo.ACTIVO);
		grupo.setFechaInicio(LocalDate.now().minusDays(30));
		grupo.setFechaFin(LocalDate.now().plusDays(30));
		nota.setId(ID_NOTA);
		nota.setNombre("Parcial 1");
		nota.setGrupo(grupo);
		nota.setEstado(EstadoNota.ACTIVO);
		nota.setFechaRegistroDesde(Instant.now().minusSeconds(3600));
		nota.setFechaRegistroHasta(Instant.now().plusSeconds(3600));
		var estudiante = new EstudianteEntidad();
		estudiante.setId(ID_ESTUDIANTE);
		estudiante.setNombre("Estudiante Demo");
		var estudianteGrupo = new EstudianteGrupoEntidad();
		estudianteGrupo.setId(UUID.randomUUID());
		estudianteGrupo.setEstudiante(estudiante);
		estudianteGrupo.setGrupo(grupo);

		when(grupoRepositorio.existePorId(ID_GRUPO)).thenReturn(true);
		when(grupoRepositorio.consultarPorId(ID_GRUPO)).thenReturn(Optional.of(grupo));
		when(docenteRepositorio.existePorId(ID_DOCENTE)).thenReturn(true);
		when(docenteRepositorio.consultarPorId(ID_DOCENTE)).thenReturn(Optional.of(docente));
		when(estudianteRepositorio.existePorId(ID_ESTUDIANTE)).thenReturn(true);
		when(estudianteGrupoRepositorio.existePorEstudianteYGrupo(ID_ESTUDIANTE, ID_GRUPO)).thenReturn(true);
		when(estudianteGrupoRepositorio.consultarPorEstudianteYGrupo(ID_ESTUDIANTE, ID_GRUPO)).thenReturn(Optional.of(estudianteGrupo));
		when(notaRepositorio.existePorId(ID_NOTA)).thenReturn(true);
		when(notaRepositorio.consultarPorId(ID_NOTA)).thenReturn(Optional.of(nota));
	}

	private RegistrarNotaEstudianteDominio datos(final UUID docenteQueRegistra) {
		return new RegistrarNotaEstudianteDominio(docenteQueRegistra, ID_GRUPO, ID_ESTUDIANTE, ID_NOTA, new BigDecimal("3.5"));
	}

	private String error(final RegistrarNotaEstudianteDominio datos) {
		return assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(datos)).getMensajeUsuario();
	}

	@Test
	void guardaLaNotaEscaladaYNotificaAlEstudiante() {
		casoUso.ejecutar(datos(ID_DOCENTE));

		var guardada = ArgumentCaptor.forClass(NotaEstudianteEntidad.class);
		verify(notaEstudianteRepositorio).crear(guardada.capture());
		assertEquals(350, guardada.getValue().getValor());
		assertEquals(nota, guardada.getValue().getNota());
		var aviso = ArgumentCaptor.forClass(NotificacionDTO.class);
		verify(notificacion).notificar(aviso.capture());
		assertEquals("Hola Estudiante Demo, se registró tu nota en Parcial 1: 3.50", aviso.getValue().contenido());
	}

	@Test
	void rechazaUnGrupoQueNoExiste() {
		when(grupoRepositorio.existePorId(ID_GRUPO)).thenReturn(false);
		assertEquals("El grupo con ID " + ID_GRUPO + " no existe", error(datos(ID_DOCENTE)));
	}

	@Test
	void rechazaUnGrupoInactivoOFueraDeFechas() {
		grupo.setEstado(EstadoGrupo.INACTIVO);
		assertEquals("El grupo con ID " + ID_GRUPO + " no está activo", error(datos(ID_DOCENTE)));
		grupo.setEstado(EstadoGrupo.ACTIVO);
		grupo.setFechaFin(LocalDate.now().minusDays(1));
		assertEquals("El grupo con ID " + ID_GRUPO + " no está vigente", error(datos(ID_DOCENTE)));
	}

	@Test
	void rechazaUnDocenteInactivoONoAsignado() {
		docente.setEstado(EstadoDocente.INACTIVO);
		assertEquals("El docente con ID " + ID_DOCENTE + " no está activo", error(datos(ID_DOCENTE)));

		var otroDocente = UUID.randomUUID();
		var otro = new DocenteEntidad();
		otro.setId(otroDocente);
		otro.setEstado(EstadoDocente.ACTIVO);
		when(docenteRepositorio.existePorId(otroDocente)).thenReturn(true);
		when(docenteRepositorio.consultarPorId(otroDocente)).thenReturn(Optional.of(otro));
		assertEquals("El docente con ID " + otroDocente + " no está asignado al grupo " + ID_GRUPO, error(datos(otroDocente)));
	}

	@Test
	void rechazaUnEstudianteQueNoExisteONoEstaInscrito() {
		when(estudianteGrupoRepositorio.existePorEstudianteYGrupo(ID_ESTUDIANTE, ID_GRUPO)).thenReturn(false);
		assertEquals("El estudiante con ID " + ID_ESTUDIANTE + " no pertenece al grupo " + ID_GRUPO, error(datos(ID_DOCENTE)));
		when(estudianteRepositorio.existePorId(ID_ESTUDIANTE)).thenReturn(false);
		assertEquals("El estudiante con ID " + ID_ESTUDIANTE + " no existe", error(datos(ID_DOCENTE)));
	}

	@Test
	void rechazaUnaNotaInactivaDeOtroGrupoOCerrada() {
		nota.setEstado(EstadoNota.INACTIVO);
		assertEquals("La nota con ID " + ID_NOTA + " no está activa", error(datos(ID_DOCENTE)));
		nota.setEstado(EstadoNota.ACTIVO);
		var otroGrupo = new GrupoEntidad();
		otroGrupo.setId(UUID.randomUUID());
		nota.setGrupo(otroGrupo);
		assertEquals("La nota con ID " + ID_NOTA + " no pertenece al grupo " + ID_GRUPO, error(datos(ID_DOCENTE)));
		nota.setGrupo(grupo);
		nota.setFechaRegistroHasta(Instant.now().minusSeconds(60));
		assertEquals("La fecha para registrar la nota con ID " + ID_NOTA + " no está vigente", error(datos(ID_DOCENTE)));
	}

	@Test
	void rechazaUnaNotaYaRegistradaSinGuardarNada() {
		when(notaEstudianteRepositorio.existePorEstudianteYNota(ID_ESTUDIANTE, ID_NOTA)).thenReturn(true);

		assertEquals("El estudiante con ID " + ID_ESTUDIANTE + " ya tiene registrada la nota " + ID_NOTA, error(datos(ID_DOCENTE)));
		verify(notaEstudianteRepositorio, never()).crear(any());
		verify(notificacion, never()).notificar(any());
	}

	@Test
	void validaElValorDespuesDeLasReglasDeLaNota() {
		var tresDecimales = new RegistrarNotaEstudianteDominio(ID_DOCENTE, ID_GRUPO, ID_ESTUDIANTE, ID_NOTA, new BigDecimal("3.555"));
		assertEquals("El valor de la nota debe tener máximo dos decimales", error(tresDecimales));
		var mayorACinco = new RegistrarNotaEstudianteDominio(ID_DOCENTE, ID_GRUPO, ID_ESTUDIANTE, ID_NOTA, new BigDecimal("5.01"));
		assertEquals("El valor de la nota debe estar entre 0 y 5", error(mayorACinco));
		verify(notaEstudianteRepositorio, never()).crear(any());
	}

	@Test
	void conDatosVaciosValidaPrimeroElGrupo() {
		assertEquals("El ID del grupo no puede ser nulo",
				error(new RegistrarNotaEstudianteDominio(null, null, null, null, null)));
	}

}