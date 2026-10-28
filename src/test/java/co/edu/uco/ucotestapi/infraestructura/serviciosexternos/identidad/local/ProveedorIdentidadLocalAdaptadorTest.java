package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.identidad.local;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.identidad.local.ProveedorIdentidadLocalAdaptador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.seguridad.ClaimsToken;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.seguridad.ClaveJwt;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.seguridad.SeguridadConfiguracion;
import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.DatosSesionDTO;

class ProveedorIdentidadLocalAdaptadorTest {

	private final SeguridadConfiguracion configuracion = new SeguridadConfiguracion();
	private final javax.crypto.SecretKey clave = ClaveJwt.desde("una-clave-de-prueba-con-mas-de-32-caracteres", true);
	private final ProveedorIdentidadLocalAdaptador proveedor = new ProveedorIdentidadLocalAdaptador(new BCryptPasswordEncoder(4),
			configuracion.codificadorTokens(clave), 2);

	@Test
	void cifraLaClaveConBcryptYLaVerifica() {
		var cifrada = proveedor.cifrarClave("Clave2026");

		assertNotEquals("Clave2026", cifrada);
		assertTrue(cifrada.startsWith("$2"));
		assertTrue(proveedor.claveCoincide("Clave2026", cifrada));
		assertFalse(proveedor.claveCoincide("Otra2026", cifrada));
	}

	@Test
	void emiteUnTokenFirmadoQueSePuedeLeerConLaMismaClave() {
		var idUsuario = UUID.randomUUID();
		var idPersona = UUID.randomUUID();

		var token = proveedor.emitirToken(new DatosSesionDTO(idUsuario, "ana@b.co", "Ana Ruiz", RolUsuario.DOCENTE, idPersona));
		var jwt = configuracion.decodificadorTokens(clave).decode(token.token());

		assertEquals(idUsuario.toString(), jwt.getSubject());
		assertEquals("DOCENTE", jwt.getClaimAsString(ClaimsToken.ROL));
		assertEquals(idPersona.toString(), jwt.getClaimAsString(ClaimsToken.ID_PERSONA));
		assertEquals("Ana Ruiz", jwt.getClaimAsString(ClaimsToken.NOMBRE));
		assertEquals(ClaimsToken.EMISOR, jwt.getClaimAsString("iss"));
		assertEquals(Duration.ofHours(2), Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()));
	}

}