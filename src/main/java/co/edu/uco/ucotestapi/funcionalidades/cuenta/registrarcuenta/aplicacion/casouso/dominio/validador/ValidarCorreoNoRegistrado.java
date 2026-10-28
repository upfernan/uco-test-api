package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador;

import co.edu.uco.ucotestapi.aplicacion.casouso.ParametrosEjecucion;
import co.edu.uco.ucotestapi.aplicacion.casouso.validador.Validador;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;

// Regla de registro 2: el correo no puede tener otra cuenta
public final class ValidarCorreoNoRegistrado implements Validador {
	private static final Validador INSTANCIA = new ValidarCorreoNoRegistrado();

	private ValidarCorreoNoRegistrado() {
		super();
	}

	public static void ejecutarValidacion(final String correo, final UsuarioRepositorio repositorio) {
		INSTANCIA.validar(correo, repositorio);
	}

	@Override
	public void validar(final Object... datos) {
		var correo = ParametrosEjecucion.obtener(datos, 0, String.class, getClass());
		var repositorio = ParametrosEjecucion.obtener(datos, 1, UsuarioRepositorio.class, getClass());
		if (repositorio.existePorCorreo(correo)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.CUENTA_CORREO_YA_REGISTRADO);
		}
	}

}