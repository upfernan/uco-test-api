package co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.casouso.dominio;

import java.util.Locale;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Correo de quien pide recuperar la contrasena. No se valida: la respuesta debe ser igual exista o no la cuenta.
 */
public class SolicitarRecuperacionClaveDominio {
	private final String correo;

	public SolicitarRecuperacionClaveDominio(final String correo) {
		this.correo = UtilTexto.aplicarTrim(correo).toLowerCase(Locale.ROOT);
	}

	public String getCorreo() {
		return correo;
	}

}
