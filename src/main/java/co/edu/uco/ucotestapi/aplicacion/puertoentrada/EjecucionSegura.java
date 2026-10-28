package co.edu.uco.ucotestapi.aplicacion.puertoentrada;

import java.util.function.Supplier;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

/**
 * Ejecuta el trabajo de un interactor: deja pasar las excepciones controladas y envuelve cualquier otro error en una
 * UcoTestApiExcepcion con los mensajes del catálogo, para que nunca llegue al usuario un error técnico.
 */
public final class EjecucionSegura {

	private EjecucionSegura() {
	}

	public static <R> R ejecutar(final Supplier<R> trabajo, final CodigoMensaje mensajeUsuario,
			final CodigoMensaje mensajeTecnico) {
		try {
			return trabajo.get();
		} catch (UcoTestApiExcepcion excepcion) {
			throw excepcion;
		} catch (RuntimeException excepcion) {
			throw UcoTestApiExcepcion.crear(excepcion, CatalogoMensajesProveedor.contenido(mensajeUsuario),
					CatalogoMensajesProveedor.contenido(mensajeTecnico, excepcion.getClass().getSimpleName()));
		}
	}

}