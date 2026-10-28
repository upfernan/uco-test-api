package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import java.time.LocalDate;

import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class FechaEnRangoEspecificacion extends Especificacion<LocalDate> {
	private final LocalDate desde;
	private final LocalDate hasta;

	public FechaEnRangoEspecificacion(final LocalDate desde, final LocalDate hasta) {
		this.desde = desde;
		this.hasta = hasta;
	}

	@Override
	public boolean esSatisfechaPor(final LocalDate dato) {
		return dato != null && desde != null && hasta != null && !dato.isBefore(desde) && !dato.isAfter(hasta);
	}

}
