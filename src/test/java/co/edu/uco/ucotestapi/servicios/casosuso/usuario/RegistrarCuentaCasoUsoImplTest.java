package co.edu.uco.ucotestapi.servicios.casosuso.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.support.ResourceBundleMessageSource;

import co.edu.uco.ucotestapi.dominio.enums.EstadoDocente;
import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import co.edu.uco.ucotestapi.infraestructura.output.serviciosexternos.messagecatalog.CatalogoMensajesAdapter;
import co.edu.uco.ucotestapi.servicios.input.dto.request.RegistrarCuentaDTO;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.DocenteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.EstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.UsuarioRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.UsuarioEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.ProveedorIdentidadServicio;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

class RegistrarCuentaCasoUsoImplTest {

	private final UsuarioRepositorio usuarioRepositorio = mock(UsuarioRepositorio.class);
	private final DocenteRepositorio docenteRepositorio = mock(DocenteRepositorio.class);
	private final EstudianteRepositorio estudianteRepositorio = mock(EstudianteRepositorio.class);
	private final ProveedorIdentidadServicio proveedorIdentidad = mock(ProveedorIdentidadServicio.class);
	private RegistrarCuentaCasoUsoImpl casoUso;

	@BeforeEach
	void preparar() {
		var mensajes = new ResourceBundleMessageSource();
		mensajes.setBasename("mensajes");
		mensajes.setDefaultEncoding("UTF-8");
		casoUso = new RegistrarCuentaCasoUsoImpl(usuarioRepositorio, docenteRepositorio, estudianteRepositorio,
				proveedorIdentidad, new CatalogoMensajesAdapter(mensajes));
		when(proveedorIdentidad.cifrarClave("Clave2026")).thenReturn("hash-bcrypt");
		when(docenteRepositorio.consultarPorCorreo(any())).thenReturn(Optional.empty());
		when(estudianteRepositorio.consultarPorCorreo(any())).thenReturn(Optional.empty());
	}

	@Test
	void registraUnDocenteNuevoActivoYGuardaLaClaveCifrada() {
		var cuenta = casoUso.ejecutar(new RegistrarCuentaDTO("Ana Ruiz", "Ana@Correo.co", "Clave2026", "docente"));

		var docente = ArgumentCaptor.forClass(DocenteEntidad.class);
		verify(docenteRepositorio).crear(docente.capture());
		assertEquals(EstadoDocente.ACTIVO, docente.getValue().getEstado());
		assertEquals("ana@correo.co", docente.getValue().getCorreo());

		var usuario = ArgumentCaptor.forClass(UsuarioEntidad.class);
		verify(usuarioRepositorio).crear(usuario.capture());
		assertEquals("hash-bcrypt", usuario.getValue().claveHash());
		assertEquals(RolUsuario.DOCENTE, usuario.getValue().rol());
		assertEquals(docente.getValue().getId(), cuenta.idPersona());
		assertEquals("DOCENTE", cuenta.rol());
	}

	@Test
	void asociaLaCuentaAUnDocenteQueYaExisteConEseCorreo() {
		var existente = new DocenteEntidad();
		existente.setId(UUID.randomUUID());
		when(docenteRepositorio.consultarPorCorreo("docente.demo@uco-test-api.test")).thenReturn(Optional.of(existente));

		var cuenta = casoUso.ejecutar(new RegistrarCuentaDTO("Docente Demo", "docente.demo@uco-test-api.test", "Clave2026", "DOCENTE"));

		verify(docenteRepositorio, never()).crear(any());
		assertEquals(existente.getId(), cuenta.idPersona());
	}

	@Test
	void registraUnEstudianteNuevo() {
		var cuenta = casoUso.ejecutar(new RegistrarCuentaDTO("Laura Gómez", "laura@b.co", "Clave2026", "ESTUDIANTE"));

		var estudiante = ArgumentCaptor.forClass(EstudianteEntidad.class);
		verify(estudianteRepositorio).crear(estudiante.capture());
		assertEquals(estudiante.getValue().getId(), cuenta.idPersona());
		assertEquals("ESTUDIANTE", cuenta.rol());
	}

	@Test
	void asociaLaCuentaAUnEstudianteQueYaExisteConEseCorreo() {
		var existente = new EstudianteEntidad();
		existente.setId(UUID.randomUUID());
		when(estudianteRepositorio.consultarPorCorreo("estudiante.demo@uco-test-api.test")).thenReturn(Optional.of(existente));

		var cuenta = casoUso.ejecutar(new RegistrarCuentaDTO("Estudiante Demo", "estudiante.demo@uco-test-api.test", "Clave2026", "ESTUDIANTE"));

		verify(estudianteRepositorio, never()).crear(any());
		assertEquals(existente.getId(), cuenta.idPersona());
	}

	@Test
	void traduceLaReglaIncumplidaAlMensajeDelCatalogo() {
		when(usuarioRepositorio.existePorCorreo("ana@b.co")).thenReturn(true);

		var datos = new RegistrarCuentaDTO("Ana", "ana@b.co", "Clave2026", "DOCENTE");
		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(datos));

		assertEquals("Ya existe una cuenta registrada con ese correo", error.getMensajeUsuario());
		verify(usuarioRepositorio, never()).crear(any());
	}

	@Test
	void rechazaDatosNulosConElMensajeDelRol() {
		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(null));

		assertEquals("El rol es obligatorio y debe ser DOCENTE o ESTUDIANTE", error.getMensajeUsuario());
	}

	@Test
	void envuelveErroresInesperadosSinExponerDetalles() {
		when(proveedorIdentidad.cifrarClave(any())).thenThrow(new IllegalStateException("detalle interno"));

		var datos = new RegistrarCuentaDTO("Ana", "ana@b.co", "Clave2026", "DOCENTE");
		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(datos));

		assertEquals("Ocurrió un error inesperado al registrar la cuenta. Inténtelo de nuevo.", error.getMensajeUsuario());
	}

}