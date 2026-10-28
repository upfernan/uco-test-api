package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador;

import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.FormatoTextoEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.LongitudTextoEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.TextoPresenteEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales.CumpleEspecificacionRegla;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.parametros.CatalogoParametros;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

// Regla de registro 1: obligatorio, de 2 a 100 caracteres, solo letras y espacios
public final class ValidarNombreCuenta {
	private static final Especificacion<String> NOMBRE_VALIDO = new TextoPresenteEspecificacion()
			.y(new LongitudTextoEspecificacion(CatalogoParametros.NOMBRE_LONGITUD_MINIMA, CatalogoParametros.NOMBRE_LONGITUD_MAXIMA))
			.y(new FormatoTextoEspecificacion(CatalogoParametros.NOMBRE_PATRON));

	private ValidarNombreCuenta() {
		super();
	}

	public static void ejecutarValidacion(final String nombre) {
		CumpleEspecificacionRegla.ejecutarRegla(nombre, NOMBRE_VALIDO, CodigoMensaje.CUENTA_NOMBRE_INVALIDO);
	}

}