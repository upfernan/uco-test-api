package co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.TokenDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.dominio.IniciarSesionDominio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;
import co.edu.uco.ucotestapi.soporte.CatalogoMensajesPrueba;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoDocente;
import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

class IniciarSesionCasoUsoImplTest {
	private static final String CREDENCIALES_INVALIDAS = "Correo o contraseña incorrectos";

	private final UsuarioRepositorio usuarioRepositorio = mock(UsuarioRepositorio.class);
	private final DocenteRepositorio docenteRepositorio = mock(DocenteRepositorio.class);
	private final EstudianteRepositorio estudianteRepositorio = mock(EstudianteRepositorio.class);
	private final ProveedorIdentidadPuertoSalida proveedorIdentidad = mock(ProveedorIdentidadPuertoSalida.class);
	private final UUID idDocente = UUID.randomUUID();
	private final UUID idEstudiante = UUID.randomUUID();
	private final DocenteEntidad docente = new DocenteEntidad();
	private IniciarSesionCasoUsoImpl casoUso;

	@BeforeAll
	static void prepararCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	@BeforeEach
	void preparar() {
		casoUso = new IniciarSesionCasoUsoImpl(usuarioRepositorio, docenteRepositorio, estudianteRepositorio, proveedorIdentidad);
		docente.setId(idDocente);
		docente.setNombre("Docente Demo");
		docente.setEstado(EstadoDocente.ACTIVO);
		when(docenteRepositorio.consultarPorId(idDocente)).thenReturn(Optional.of(docente));
		var estudiante = new EstudianteEntidad();
		estudiante.setId(idEstudiante);
		estudiante.setNombre("Laura Gómez");
		when(estudianteRepositorio.consultarPorId(idEstudiante)).thenReturn(Optional.of(estudiante));
		when(usuarioRepositorio.consultarPorCorreo("docente@b.co"))
				.thenReturn(Optional.of(UsuarioEntidad.deDocente(UUID.randomUUID(), "docente@b.co", "hash", idDocente)));
		when(usuarioRepositorio.consultarPorCorreo("laura@b.co"))
				.thenReturn(Optional.of(UsuarioEntidad.deEstudiante(UUID.randomUUID(), "laura@b.co", "hash", idEstudiante)));
		when(proveedorIdentidad.claveCoincide("Clave2026", "hash")).thenReturn(true);
		when(proveedorIdentidad.emitirToken(any())).thenReturn(new TokenDTO("token-firmado", Instant.now()));
	}

	private String error(final IniciarSesionDominio credenciales) {
		return assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(credenciales)).getMensajeUsuario();
	}

	@Test
	void iniciaSesionDeUnDocenteActivo() {
		var sesion = casoUso.ejecutar(new IniciarSesionDominio(" Docente@B.co ", "Clave2026"));

		assertEquals("token-firmado", sesion.token());
		assertEquals(RolUsuario.DOCENTE, sesion.rol());
		assertEquals("Docente Demo", sesion.nombre());
		assertEquals(idDocente, sesion.idPersona());
	}

	@Test
	void iniciaSesionDeUnEstudiante() {
		var sesion = casoUso.ejecutar(new IniciarSesionDominio("laura@b.co", "Clave2026"));

		assertEquals(RolUsuario.ESTUDIANTE, sesion.rol());
		assertEquals(idEstudiante, sesion.idPersona());
	}

	@Test
	void conClaveIncorrectaOCorreoInexistenteDaElMismoMensaje() {
		when(usuarioRepositorio.consultarPorCorreo("nadie@b.co")).thenReturn(Optional.empty());

		assertEquals(CREDENCIALES_INVALIDAS, error(new IniciarSesionDominio("docente@b.co", "Otra2026")));
		assertEquals(CREDENCIALES_INVALIDAS, error(new IniciarSesionDominio("nadie@b.co", "Clave2026")));
		assertEquals(CREDENCIALES_INVALIDAS, error(new IniciarSesionDominio(null, null)));
		verify(proveedorIdentidad, never()).emitirToken(any());
	}

	@Test
	void unDocenteInactivoNoPuedeIniciarSesion() {
		docente.setEstado(EstadoDocente.INACTIVO);

		assertEquals("El docente con ID " + idDocente + " no está activo", error(new IniciarSesionDominio("docente@b.co", "Clave2026")));
		verify(proveedorIdentidad, never()).emitirToken(any());
	}

}