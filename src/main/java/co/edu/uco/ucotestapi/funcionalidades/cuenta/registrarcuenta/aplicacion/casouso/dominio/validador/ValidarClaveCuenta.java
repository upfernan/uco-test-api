package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador;

import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.ContieneTextoEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.LongitudTextoEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales.CumpleEspecificacionRegla;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.parametros.CatalogoParametros;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

// Regla de registro 3: minimo 8 caracteres, con al menos una letra y un numero
public final class ValidarClaveCuenta {
	private static final Especificacion<String> CLAVE_SEGURA = new LongitudTextoEspecificacion(CatalogoParametros.CLAVE_LONGITUD_MINIMA, Integer.MAX_VALUE)
			.y(new ContieneTextoEspecificacion(CatalogoParametros.CLAVE_TIENE_LETRA))
			.y(new ContieneTextoEspecificacion(CatalogoParametros.CLAVE_TIENE_NUMERO));

	private ValidarClaveCuenta() {
		super();
	}

	public static void ejecutarValidacion(final String clave) {
		CumpleEspecificacionRegla.ejecutarRegla(clave, CLAVE_SEGURA, CodigoMensaje.CUENTA_CLAVE_INSEGURA);
	}

}