package co.edu.uco.ucotestapi.aplicacion.casouso.validador.generales.cuenta;

import co.edu.uco.ucotestapi.aplicacion.casouso.ParametrosEjecucion;
import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.ContieneTextoEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.LongitudTextoEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales.CumpleEspecificacionRegla;
import co.edu.uco.ucotestapi.aplicacion.casouso.validador.Validador;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;
import co.edu.uco.ucotestapi.transversal.parametros.CatalogoParametros;

/**
 * Contrasena segura: minimo 8 caracteres, con al menos una letra y un numero.
 * La usan el registro de cuenta (regla de registro 3) y el restablecimiento de la contrasena.
 */
public final class ValidarClaveSegura implements Validador {
	private static final Validador INSTANCIA = new ValidarClaveSegura();
	private static final Especificacion<String> CLAVE_SEGURA = new LongitudTextoEspecificacion(CatalogoParametros.CLAVE_LONGITUD_MINIMA, Integer.MAX_VALUE)
			.y(new ContieneTextoEspecificacion(CatalogoParametros.CLAVE_TIENE_LETRA))
			.y(new ContieneTextoEspecificacion(CatalogoParametros.CLAVE_TIENE_NUMERO));

	private ValidarClaveSegura() {
		super();
	}

	public static void ejecutarValidacion(final String clave) {
		INSTANCIA.validar(clave);
	}

	@Override
	public void validar(final Object... datos) {
		var clave = ParametrosEjecucion.obtener(datos, 0, String.class, getClass());
		CumpleEspecificacionRegla.ejecutarRegla(clave, CLAVE_SEGURA, CodigoMensaje.CUENTA_CLAVE_INSEGURA);
	}

}
