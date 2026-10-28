package co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales;

import java.util.UUID;

import co.edu.uco.ucotestapi.aplicacion.casouso.ParametrosEjecucion;
import co.edu.uco.ucotestapi.aplicacion.casouso.regla.Regla;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ConsultarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;

/**
 * Regla generica de existencia: sirve para docente, grupo, estudiante y nota con el mismo codigo.
 */
public final class ExistePorIdRegla implements Regla {
	private static final Regla INSTANCIA = new ExistePorIdRegla();

	private ExistePorIdRegla() {
		super();
	}

	public static void ejecutarRegla(final UUID id, final ConsultarRepositorio<UUID, ?> repositorio,
			final CodigoMensaje codigoSiNoExiste) {
		INSTANCIA.ejecutar(id, repositorio, codigoSiNoExiste);
	}

	@Override
	public void ejecutar(final Object... datos) {
		var id = ParametrosEjecucion.obtener(datos, 0, UUID.class, getClass());
		var repositorio = ParametrosEjecucion.obtener(datos, 1, ConsultarRepositorio.class, getClass());
		var codigo = ParametrosEjecucion.obtener(datos, 2, CodigoMensaje.class, getClass());
		if (!repositorio.existePorId(id)) {
			throw CatalogoMensajesProveedor.excepcion(codigo, id);
		}
	}

}