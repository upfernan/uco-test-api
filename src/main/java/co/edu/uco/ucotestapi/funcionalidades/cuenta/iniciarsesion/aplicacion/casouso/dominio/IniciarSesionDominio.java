package co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.dominio;

import java.util.Locale;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Credenciales para iniciar sesion. No se validan campo por campo: la regla 7 exige no revelar que fallo.
 */
public class IniciarSesionDominio {
	private final String correo;
	private final String clave;

	public IniciarSesionDominio(final String correo, final String clave) {
		this.correo = UtilTexto.aplicarTrim(correo).toLowerCase(Locale.ROOT);
		this.clave = UtilTexto.obtenerValorDefecto(clave);
	}

	public String getCorreo() {
		return correo;
	}

	public String getClave() {
		return clave;
	}

}