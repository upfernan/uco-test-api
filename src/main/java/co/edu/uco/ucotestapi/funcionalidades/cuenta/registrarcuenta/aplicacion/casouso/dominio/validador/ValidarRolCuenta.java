package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;

// Regla de registro 4: el rol debe ser DOCENTE o ESTUDIANTE
public final class ValidarRolCuenta {

	private ValidarRolCuenta() {
		super();
	}

	public static RolUsuario ejecutarValidacion(final String rol) {
		for (var rolValido : RolUsuario.values()) {
			if (rolValido.name().equals(rol)) {
				return rolValido;
			}
		}
		throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.CUENTA_ROL_INVALIDO);
	}

}