package co.edu.uco.ucotestapi.aplicacion.casouso.validador.generales.docente;

import co.edu.uco.ucotestapi.aplicacion.casouso.ParametrosEjecucion;
import co.edu.uco.ucotestapi.aplicacion.casouso.validador.Validador;
import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.EsIgualEspecificacion;
import co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales.CumpleEspecificacionRegla;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoDocente;

public final class ValidarDocenteActivo implements Validador {
	private static final Validador INSTANCIA = new ValidarDocenteActivo();
	private static final EsIgualEspecificacion ACTIVO = new EsIgualEspecificacion(EstadoDocente.ACTIVO);

	private ValidarDocenteActivo() {
		super();
	}

	public static void ejecutarValidacion(final DocenteEntidad docente) {
		INSTANCIA.validar(docente);
	}

	@Override
	public void validar(final Object... datos) {
		var docente = ParametrosEjecucion.obtener(datos, 0, DocenteEntidad.class, getClass());
		CumpleEspecificacionRegla.ejecutarRegla(docente.getEstado(), ACTIVO, CodigoMensaje.DOCENTE_NO_ACTIVO, docente.getId());
	}

}