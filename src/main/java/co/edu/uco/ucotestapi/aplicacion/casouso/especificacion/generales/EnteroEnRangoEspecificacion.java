package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilNumero;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class EnteroEnRangoEspecificacion extends Especificacion<Integer> {
	private final int minimo;
	private final int maximo;

	public EnteroEnRangoEspecificacion(final int minimo, final int maximo) {
		this.minimo = minimo;
		this.maximo = maximo;
	}

	@Override
	public boolean esSatisfechaPor(final Integer dato) {
		return dato != null && UtilNumero.estaEntre(dato, minimo, maximo);
	}

}
