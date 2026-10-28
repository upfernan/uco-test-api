package co.edu.uco.ucotestapi.infraestructura.configuracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ClaveJwtTest {

	private static final String SECRETO_VALIDO = "una-clave-de-prueba-con-mas-de-32-caracteres";

	@Test
	void usaElSecretoConfigurado() {
		var clave = ClaveJwt.desde(SECRETO_VALIDO, true);

		assertEquals("HmacSHA256", clave.getAlgorithm());
		assertEquals(SECRETO_VALIDO.length(), clave.getEncoded().length);
	}

	@Test
	void enLocalSinSecretoGeneraUnaClaveTemporal() {
		var primera = ClaveJwt.desde("", false);
		var segunda = ClaveJwt.desde(null, false);

		assertEquals(ClaveJwt.BYTES_MINIMOS, primera.getEncoded().length);
		assertNotEquals(new String(primera.getEncoded()), new String(segunda.getEncoded()));
	}

	@Test
	void enLaNubeSinSecretoNoArranca() {
		var error = assertThrows(IllegalStateException.class, () -> ClaveJwt.desde("  ", true));

		assertEquals(true, error.getMessage().contains("JWT_SECRET"));
	}

	@Test
	void rechazaUnSecretoCortoSinMostrarlo() {
		var error = assertThrows(IllegalStateException.class, () -> ClaveJwt.desde("corta-secreta", true));

		assertEquals(false, error.getMessage().contains("corta-secreta"));
	}

}