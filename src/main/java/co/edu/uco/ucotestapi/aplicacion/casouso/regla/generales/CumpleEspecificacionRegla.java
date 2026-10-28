package co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales;

import co.edu.uco.ucotestapi.aplicacion.casouso.ParametrosEjecucion;
import co.edu.uco.ucotestapi.aplicacion.casouso.regla.Regla;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

/**
 * Regla generica: si el dato no cumple la especificacion, lanza la excepcion con el mensaje del catalogo indicado.
 */
public final class CumpleEspecificacionRegla implements Regla {
	private static final Regla INSTANCIA = new CumpleEspecificacionRegla();

	private CumpleEspecificacionRegla() {
		super();
	}

	public static <T> void ejecutarRegla(final T dato, final Especificacion<T> especificacion, final CodigoMensaje codigo,
			final Object... parametrosMensaje) {
		INSTANCIA.ejecutar(dato, especificacion, codigo, parametrosMensaje);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void ejecutar(final Object... datos) {
		var dato = datos == null || datos.length == 0 ? null : datos[0];
		var especificacion = (Especificacion<Object>) ParametrosEjecucion.obtener(datos, 1, Especificacion.class, getClass());
		var codigo = ParametrosEjecucion.obtener(datos, 2, CodigoMensaje.class, getClass());
		var parametrosMensaje = ParametrosEjecucion.obtener(datos, 3, Object[].class, getClass());
		if (!especificacion.esSatisfechaPor(dato)) {
			throw CatalogoMensajesProveedor.excepcion(codigo, parametrosMensaje);
		}
	}

}