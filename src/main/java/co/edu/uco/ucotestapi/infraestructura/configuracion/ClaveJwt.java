package co.edu.uco.ucotestapi.infraestructura.configuracion;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Clave para firmar los tokens. En la nube llega como JWT_SECRET desde el vault (Infisical).
 * Sin ella, en la nube la aplicacion no arranca; en local se genera una temporal.
 */
public final class ClaveJwt {
	public static final int BYTES_MINIMOS = 32;
	private static final String ALGORITMO = "HmacSHA256";
	private static final SecureRandom ALEATORIO = new SecureRandom();

	private ClaveJwt() {
		super();
	}

	public static SecretKey desde(final String secreto, final boolean entornoNube) {
		var valor = UtilTexto.aplicarTrim(secreto);
		if (valor.isEmpty()) {
			if (entornoNube) {
				throw new IllegalStateException("Falta JWT_SECRET: configúrelo en el vault con al menos " + BYTES_MINIMOS + " caracteres");
			}
			return generarTemporal();
		}
		var bytes = valor.getBytes(StandardCharsets.UTF_8);
		if (bytes.length < BYTES_MINIMOS) {
			throw new IllegalStateException("JWT_SECRET es muy corta: debe tener al menos " + BYTES_MINIMOS + " caracteres");
		}
		return new SecretKeySpec(bytes, ALGORITMO);
	}

	private static SecretKey generarTemporal() {
		var bytes = new byte[BYTES_MINIMOS];
		ALEATORIO.nextBytes(bytes);
		return new SecretKeySpec(bytes, ALGORITMO);
	}

}