package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import co.edu.uco.ucotestapi.soporte.CatalogoMensajesPrueba;
import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

class RegistrarCuentaDominioTest {
	private static final String NOMBRE_INVALIDO = "El nombre es obligatorio, debe tener entre 2 y 100 caracteres y solo letras y espacios";
	private static final String CORREO_INVALIDO = "El correo es obligatorio y debe tener un formato válido";
	private static final String CLAVE_INSEGURA = "La contraseña debe tener mínimo 8 caracteres, con al menos una letra y un número";
	private static final String ROL_INVALIDO = "El rol es obligatorio y debe ser DOCENTE o ESTUDIANTE";

	@BeforeAll
	static void prepararCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	private static String error(final String nombre, final String correo, final String clave, final String rol) {
		return assertThrows(UcoTestApiExcepcion.class, () -> new RegistrarCuentaDominio(nombre, correo, clave, rol))
				.getMensajeUsuario();
	}

	@Test
	void normalizaNombreCorreoYRol() {
		var cuenta = new RegistrarCuentaDominio("  Ana Ruiz ", "  Ana.Ruiz@Correo.CO ", "Clave2026", " docente ");

		assertEquals("Ana Ruiz", cuenta.getNombre());
		assertEquals("ana.ruiz@correo.co", cuenta.getCorreo());
		assertEquals(RolUsuario.DOCENTE, cuenta.getRol());
		assertEquals("Clave2026", cuenta.getClave());
	}

	@Test
	void aceptaNombreConTildes() {
		assertEquals("José Ñúñez", new RegistrarCuentaDominio("José Ñúñez", "a@b.co", "Clave2026", "ESTUDIANTE").getNombre());
	}

	@Test
	void validaPrimeroElRol() {
		assertEquals(ROL_INVALIDO, error(null, null, null, null));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "ADMIN", "profesor" })
	void rechazaRolesInvalidos(final String rol) {
		assertEquals(ROL_INVALIDO, error("Ana", "a@b.co", "Clave2026", rol));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "A", "Ana123", "Ana_Ruiz" })
	void rechazaNombresInvalidos(final String nombre) {
		assertEquals(NOMBRE_INVALIDO, error(nombre, "a@b.co", "Clave2026", "DOCENTE"));
	}

	@Test
	void rechazaNombreMuyLargo() {
		assertEquals(NOMBRE_INVALIDO, error("A".repeat(101), "a@b.co", "Clave2026", "DOCENTE"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "sin-arroba", "a@b", "a b@c.co", "@b.co" })
	void rechazaCorreosInvalidos(final String correo) {
		assertEquals(CORREO_INVALIDO, error("Ana", correo, "Clave2026", "DOCENTE"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "Corta1", "SoloLetras", "12345678" })
	void rechazaClavesInseguras(final String clave) {
		assertEquals(CLAVE_INSEGURA, error("Ana", "a@b.co", clave, "DOCENTE"));
	}

}