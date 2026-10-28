package co.edu.uco.ucotestapi.dominio.usuario.registrarcuenta;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.UsuarioRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;

class CuentaDominioTest {

	private final UsuarioRepositorio usuarioRepositorio = mock(UsuarioRepositorio.class);

	private CuentaDominio cuenta(String nombre, String correo, String clave, String rol) {
		return new CuentaDominio(nombre, correo, clave, rol, usuarioRepositorio);
	}

	private CodigoMensaje codigoDe(Runnable accion) {
		return assertThrows(ReglaNegocioException.class, accion::run).getCodigo();
	}

	@Test
	void normalizaCorreoYRol() {
		var cuenta = cuenta("  Ana Ruiz ", "  Ana.Ruiz@Correo.CO ", "Clave2026", " docente ");

		assertEquals("Ana Ruiz", cuenta.getNombre());
		assertEquals("ana.ruiz@correo.co", cuenta.getCorreo());
		assertEquals(RolUsuario.DOCENTE, cuenta.obtenerRol());
	}

	@Test
	void aceptaNombreConTildes() {
		assertDoesNotThrow(() -> cuenta("José Ñúñez", "a@b.co", "Clave2026", "ESTUDIANTE").validarNombre());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "A", "Ana123", "Ana_Ruiz" })
	void rechazaNombresInvalidos(String nombre) {
		assertEquals(CodigoMensaje.CUENTA_NOMBRE_INVALIDO, codigoDe(() -> cuenta(nombre, "a@b.co", "Clave2026", "DOCENTE").validarNombre()));
	}

	@Test
	void rechazaNombreMuyLargo() {
		var largo = "A".repeat(101);
		assertEquals(CodigoMensaje.CUENTA_NOMBRE_INVALIDO, codigoDe(() -> cuenta(largo, "a@b.co", "Clave2026", "DOCENTE").validarNombre()));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "sin-arroba", "a@b", "a b@c.co", "@b.co" })
	void rechazaCorreosInvalidos(String correo) {
		assertEquals(CodigoMensaje.CUENTA_CORREO_INVALIDO, codigoDe(() -> cuenta("Ana", correo, "Clave2026", "DOCENTE").validarFormatoCorreo()));
	}

	@Test
	void rechazaCorreoYaRegistrado() {
		when(usuarioRepositorio.existePorCorreo("ana@b.co")).thenReturn(true);

		assertEquals(CodigoMensaje.CUENTA_CORREO_YA_REGISTRADO, codigoDe(() -> cuenta("Ana", "ANA@b.co", "Clave2026", "DOCENTE").validarCorreoNoRegistrado()));
	}

	@Test
	void aceptaCorreoNuevo() {
		assertDoesNotThrow(() -> cuenta("Ana", "nueva@b.co", "Clave2026", "DOCENTE").validarCorreoNoRegistrado());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "Corta1", "SoloLetras", "12345678" })
	void rechazaClavesInseguras(String clave) {
		assertEquals(CodigoMensaje.CUENTA_CLAVE_INSEGURA, codigoDe(() -> cuenta("Ana", "a@b.co", clave, "DOCENTE").validarClaveSegura()));
	}

	@Test
	void aceptaClaveSegura() {
		assertDoesNotThrow(() -> cuenta("Ana", "a@b.co", "Clave2026", "DOCENTE").validarClaveSegura());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "ADMIN", "profesor" })
	void rechazaRolesInvalidos(String rol) {
		assertEquals(CodigoMensaje.CUENTA_ROL_INVALIDO, codigoDe(() -> cuenta("Ana", "a@b.co", "Clave2026", rol).validarRol()));
	}

}