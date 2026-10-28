package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador;

import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.FormatoTextoEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales.CumpleEspecificacionRegla;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.parametros.CatalogoParametros;

// Regla de registro 2: formato de correo valido
public final class ValidarCorreoCuenta {
	private static final FormatoTextoEspecificacion CORREO_VALIDO = new FormatoTextoEspecificacion(CatalogoParametros.CORREO_PATRON);

	private ValidarCorreoCuenta() {
		super();
	}

	public static void ejecutarValidacion(final String correo) {
		CumpleEspecificacionRegla.ejecutarRegla(correo, CORREO_VALIDO, CodigoMensaje.CUENTA_CORREO_INVALIDO);
	}

}