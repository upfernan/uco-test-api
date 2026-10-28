package co.edu.uco.ucotestapi.infraestructura.output.serviciosexternos.identityprovided;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.configuracion.ClaimsToken;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.DatosSesionDTO;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.ProveedorIdentidadServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.TokenDTO;

/**
 * Proveedor de identidad propio: claves con BCrypt y tokens JWT firmados con HS256.
 * Si se cambia a un proveedor externo (Auth0, Keycloak...), solo se reemplaza este adaptador.
 */
@Component
public class ProveedorIdentidadLocalAdapter implements ProveedorIdentidadServicio {
	private final PasswordEncoder codificadorClaves;
	private final JwtEncoder codificadorTokens;
	private final Duration duracionToken;

	public ProveedorIdentidadLocalAdapter(final PasswordEncoder codificadorClaves, final JwtEncoder codificadorTokens,
			@Value("${seguridad.jwt.duracion-horas:8}") final long horasDuracionToken) {
		this.codificadorClaves = codificadorClaves;
		this.codificadorTokens = codificadorTokens;
		this.duracionToken = Duration.ofHours(horasDuracionToken);
	}

	@Override
	public String cifrarClave(final String clave) {
		return codificadorClaves.encode(clave);
	}

	@Override
	public boolean claveCoincide(final String clave, final String claveCifrada) {
		return codificadorClaves.matches(clave, claveCifrada);
	}

	@Override
	public TokenDTO emitirToken(final DatosSesionDTO datosSesion) {
		var ahora = Instant.now();
		var expiraEn = ahora.plus(duracionToken);
		var claims = JwtClaimsSet.builder()
				.issuer(ClaimsToken.EMISOR)
				.subject(datosSesion.idUsuario().toString())
				.issuedAt(ahora)
				.expiresAt(expiraEn)
				.claim(ClaimsToken.ROL, datosSesion.rol().name())
				.claim(ClaimsToken.ID_PERSONA, datosSesion.idPersona().toString())
				.claim(ClaimsToken.CORREO, datosSesion.correo())
				.claim(ClaimsToken.NOMBRE, datosSesion.nombre())
				.build();
		var cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
		var token = codificadorTokens.encode(JwtEncoderParameters.from(cabecera, claims)).getTokenValue();
		return new TokenDTO(token, expiraEn);
	}

}