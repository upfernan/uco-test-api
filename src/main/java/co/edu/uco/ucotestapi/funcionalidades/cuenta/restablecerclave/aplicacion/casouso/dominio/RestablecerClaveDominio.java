package co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.casouso.dominio;

import java.util.Locale;

import co.edu.uco.ucotestapi.aplicacion.casouso.validador.generales.cuenta.ValidarClaveSegura;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Datos para cambiar la contrasena con el codigo de recuperacion. La contrasena nueva cumple la misma regla del registro.
 */
public class RestablecerClaveDominio {
	private final String correo;
	private final String codigo;
	private final String claveNueva;

	public RestablecerClaveDominio(final String correo, final String codigo, final String claveNueva) {
		this.correo = UtilTexto.aplicarTrim(correo).toLowerCase(Locale.ROOT);
		this.codigo = UtilTexto.aplicarTrim(codigo);
		this.claveNueva = UtilTexto.obtenerValorDefecto(claveNueva);
	}

	public void validarClaveNueva() {
		ValidarClaveSegura.ejecutarValidacion(claveNueva);
	}

	public String getCorreo() {
		return correo;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getClaveNueva() {
		return claveNueva;
	}

}
