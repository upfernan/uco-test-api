package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class LongitudTextoEspecificacion extends Especificacion<String> {
	private final int minimo;
	private final int maximo;

	public LongitudTextoEspecificacion(final int minimo, final int maximo) {
		this.minimo = minimo;
		this.maximo = maximo;
	}

	@Override
	public boolean esSatisfechaPor(final String dato) {
		return UtilTexto.tieneLongitudValida(dato, minimo, maximo);
	}

}
