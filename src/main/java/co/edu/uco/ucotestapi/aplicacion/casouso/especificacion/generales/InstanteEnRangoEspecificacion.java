package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import java.time.Instant;

import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class InstanteEnRangoEspecificacion extends Especificacion<Instant> {
	private final Instant desde;
	private final Instant hasta;

	public InstanteEnRangoEspecificacion(final Instant desde, final Instant hasta) {
		this.desde = desde;
		this.hasta = hasta;
	}

	@Override
	public boolean esSatisfechaPor(final Instant dato) {
		return dato != null && desde != null && hasta != null && !dato.isBefore(desde) && !dato.isAfter(hasta);
	}

}
