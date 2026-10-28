package co.edu.uco.ucotestapi.aplicacion.casouso;

import co.edu.uco.ucotestapi.aplicacion.casouso.regla.Regla;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

/**
 * Lee los parametros que reciben las reglas y validadores (Object...), y falla con un mensaje tecnico si faltan.
 * Asi cada regla no repite la misma comprobacion.
 */
public final class ParametrosEjecucion {

	private ParametrosEjecucion() {
		super();
	}

	public static <T> T obtener(final Object[] datos, final int posicion, final Class<T> tipo, final Class<?> quienLlama) {
		if (datos == null || datos.length <= posicion || !tipo.isInstance(datos[posicion])) {
			throw UcoTestApiExcepcion.crear(CatalogoMensajesProveedor.contenido(CodigoMensaje.ERROR_INESPERADO_USUARIO),
					CatalogoMensajesProveedor.contenido(CodigoMensaje.PARAMETROS_INCOMPLETOS_TECNICO, quienLlama.getSimpleName(), posicion));
		}
		return tipo.cast(datos[posicion]);
	}

}