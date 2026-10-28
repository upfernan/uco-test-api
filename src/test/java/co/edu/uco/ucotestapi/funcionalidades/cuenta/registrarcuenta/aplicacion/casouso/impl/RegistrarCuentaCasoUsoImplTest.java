package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.RegistrarCuentaDominio;
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

class RegistrarCuentaCasoUsoImplTest {
	private final UsuarioRepositorio usuarioRepositorio = mock(UsuarioRepositorio.class);
	private final DocenteRepositorio docenteRepositorio = mock(DocenteRepositorio.class);
	private final EstudianteRepositorio estudianteRepositorio = mock(EstudianteRepositorio.class);
	private final ProveedorIdentidadPuertoSalida proveedorIdentidad = mock(ProveedorIdentidadPuertoSalida.class);
	private RegistrarCuentaCasoUsoImpl casoUso;

	@BeforeAll
	static void prepararCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	@BeforeEach
	void preparar() {
		casoUso = new RegistrarCuentaCasoUsoImpl(usuarioRepositorio, docenteRepositorio, estudianteRepositorio, proveedorIdentidad);
		when(proveedorIdentidad.cifrarClave("Clave2026")).thenReturn("hash-bcrypt");
		when(docenteRepositorio.consultarPorCorreo(any())).thenReturn(Optional.empty());
		when(estudianteRepositorio.consultarPorCorreo(any())).thenReturn(Optional.empty());
	}

	@Test
	void registraUnDocenteNuevoActivoYGuardaLaClaveCifrada() {
		var cuenta = casoUso.ejecutar(new RegistrarCuentaDominio("Ana Ruiz", "Ana@Correo.co", "Clave2026", "docente"));

		var docente = ArgumentCaptor.forClass(DocenteEntidad.class);
		verify(docenteRepositorio).crear(docente.capture());
		assertEquals(EstadoDocente.ACTIVO, docente.getValue().getEstado());
		assertEquals("ana@correo.co", docente.getValue().getCorreo());
		var usuario = ArgumentCaptor.forClass(UsuarioEntidad.class);
		verify(usuarioRepositorio).crear(usuario.capture());
		assertEquals("hash-bcrypt", usuario.getValue().claveHash());
		assertEquals(RolUsuario.DOCENTE, cuenta.rol());
		assertEquals(docente.getValue().getId(), cuenta.idPersona());
	}

	@Test
	void asociaLaCuentaAUnDocenteQueYaExisteConEseCorreo() {
		var existente = new DocenteEntidad();
		existente.setId(UUID.randomUUID());
		when(docenteRepositorio.consultarPorCorreo("docente.demo@uco-test-api.test")).thenReturn(Optional.of(existente));

		var cuenta = casoUso.ejecutar(new RegistrarCuentaDominio("Docente Demo", "docente.demo@uco-test-api.test", "Clave2026", "DOCENTE"));

		verify(docenteRepositorio, never()).crear(any());
		assertEquals(existente.getId(), cuenta.idPersona());
	}

	@Test
	void registraUnEstudianteNuevoOLoAsociaSiYaExiste() {
		var nuevo = casoUso.ejecutar(new RegistrarCuentaDominio("Laura Gómez", "laura@b.co", "Clave2026", "ESTUDIANTE"));
		var estudiante = ArgumentCaptor.forClass(EstudianteEntidad.class);
		verify(estudianteRepositorio).crear(estudiante.capture());
		assertEquals(estudiante.getValue().getId(), nuevo.idPersona());

		var existente = new EstudianteEntidad();
		existente.setId(UUID.randomUUID());
		when(estudianteRepositorio.consultarPorCorreo("estudiante.demo@uco-test-api.test")).thenReturn(Optional.of(existente));
		var asociado = casoUso.ejecutar(new RegistrarCuentaDominio("Estudiante Demo", "estudiante.demo@uco-test-api.test", "Clave2026", "ESTUDIANTE"));
		assertEquals(existente.getId(), asociado.idPersona());
	}

	@Test
	void rechazaUnCorreoConCuentaSinGuardarNada() {
		when(usuarioRepositorio.existePorCorreo("ana@b.co")).thenReturn(true);
		var cuenta = new RegistrarCuentaDominio("Ana", "ana@b.co", "Clave2026", "DOCENTE");

		var error = assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(cuenta));

		assertEquals("Ya existe una cuenta registrada con ese correo", error.getMensajeUsuario());
		verify(usuarioRepositorio, never()).crear(any());
	}

}