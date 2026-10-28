package co.edu.uco.ucotestapi.servicios.casosuso.usuario;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import co.edu.uco.ucotestapi.dominio.enums.EstadoDocente;
import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import co.edu.uco.ucotestapi.infraestructura.output.serviciosexternos.messagecatalog.CatalogoMensajesAdapter;
import co.edu.uco.ucotestapi.servicios.input.dto.request.IniciarSesionDTO;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.DocenteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.EstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.UsuarioRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.UsuarioEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.ProveedorIdentidadServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.TokenDTO;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

class IniciarSesionCasoUsoImplTest {

	private static final String CREDENCIALES_INVALIDAS = "Correo o contraseña incorrectos";

	private final UsuarioRepositorio usuarioRepositorio = mock(UsuarioRepositorio.class);
	private final DocenteRepositorio docenteRepositorio = mock(DocenteRepositorio.class);
	private final EstudianteRepositorio estudianteRepositorio = mock(EstudianteRepositorio.class);
	private final ProveedorIdentidadServicio proveedorIdentidad = mock(ProveedorIdentidadServicio.class);
	private final UUID idDocente = UUID.randomUUID();
	private final UUID idEstudiante = UUID.randomUUID();
	private final DocenteEntidad docente = new DocenteEntidad();
	private IniciarSesionCasoUsoImpl casoUso;

	@BeforeEach
	void preparar() {
		var mensajes = new ResourceBundleMessageSource();
		mensajes.setBasename("mensajes");
		mensajes.setDefaultEncoding("UTF-8");
		casoUso = new IniciarSesionCasoUsoImpl(usuarioRepositorio, docenteRepositorio, estudianteRepositorio,
				proveedorIdentidad, new CatalogoMensajesAdapter(mensajes));

		docente.setId(idDocente);
		docente.setNombre("Docente Demo");
		docente.setEstado(EstadoDocente.ACTIVO);
		when(docenteRepositorio.existePorId(idDocente)).thenReturn(true);
		when(docenteRepositorio.consultarPorId(idDocente)).thenReturn(Optional.of(docente));

		var estudiante = new EstudianteEntidad();
		estudiante.setId(idEstudiante);
		estudiante.setNombre("Laura Gómez");
		when(estudianteRepositorio.consultarPorId(idEstudiante)).thenReturn(Optional.of(estudiante));

		when(usuarioRepositorio.consultarPorCorreo("docente@b.co")).thenReturn(Optional.of(usuario(RolUsuario.DOCENTE)));
		when(usuarioRepositorio.consultarPorCorreo("laura@b.co")).thenReturn(Optional.of(usuario(RolUsuario.ESTUDIANTE)));
		when(proveedorIdentidad.claveCoincide("Clave2026", "hash")).thenReturn(true);
		when(proveedorIdentidad.emitirToken(any())).thenReturn(new TokenDTO("token-firmado", Instant.now()));
	}

	private UsuarioEntidad usuario(RolUsuario rol) {
		return rol == RolUsuario.DOCENTE
				? UsuarioEntidad.deDocente(UUID.randomUUID(), "docente@b.co", "hash", idDocente)
				: UsuarioEntidad.deEstudiante(UUID.randomUUID(), "laura@b.co", "hash", idEstudiante);
	}

	@Test
	void iniciaSesionDeUnDocenteActivo() {
		var sesion = casoUso.ejecutar(new IniciarSesionDTO(" Docente@B.co ", "Clave2026"));

		assertEquals("token-firmado", sesion.token());
		assertEquals("DOCENTE", sesion.rol());
		assertEquals("Docente Demo", sesion.nombre());
		assertEquals(idDocente, sesion.idPersona());
	}

	@Test
	void iniciaSesionDeUnEstudiante() {
		var sesion = casoUso.ejecutar(new IniciarSesionDTO("laura@b.co", "Clave2026"));

		assertEquals("ESTUDIANTE", sesion.rol());
		assertEquals(idEstudiante, sesion.idPersona());
	}

	@Test
	void conClaveIncorrectaNoRevelaQueElCorreoExiste() {
		var datos = new IniciarSesionDTO("docente@b.co", "Otra2026");
		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(datos));

		assertEquals(CREDENCIALES_INVALIDAS, error.getMensajeUsuario());
		verify(proveedorIdentidad, never()).emitirToken(any());
	}

	@Test
	void conCorreoInexistenteDaElMismoMensaje() {
		when(usuarioRepositorio.consultarPorCorreo("nadie@b.co")).thenReturn(Optional.empty());

		var datos = new IniciarSesionDTO("nadie@b.co", "Clave2026");
		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(datos));

		assertEquals(CREDENCIALES_INVALIDAS, error.getMensajeUsuario());
	}

	@Test
	void unDocenteInactivoNoPuedeIniciarSesion() {
		docente.setEstado(EstadoDocente.INACTIVO);

		var datos = new IniciarSesionDTO("docente@b.co", "Clave2026");
		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(datos));

		assertEquals("El docente con ID " + idDocente + " no está activo", error.getMensajeUsuario());
		verify(proveedorIdentidad, never()).emitirToken(any());
	}

	@Test
	void conDatosNulosDaCredencialesInvalidas() {
		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(null));

		assertEquals(CREDENCIALES_INVALIDAS, error.getMensajeUsuario());
	}

	@Test
	void envuelveErroresInesperados() {
		when(proveedorIdentidad.emitirToken(any())).thenThrow(new IllegalStateException("detalle interno"));

		var datos = new IniciarSesionDTO("laura@b.co", "Clave2026");
		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(datos));

		assertEquals("Ocurrió un error inesperado al iniciar sesión. Inténtelo de nuevo.", error.getMensajeUsuario());
	}

}