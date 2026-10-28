package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import java.util.Objects;

import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class EsIgualEspecificacion extends Especificacion<Object> {
	private final Object esperado;

	public EsIgualEspecificacion(final Object esperado) {
		this.esperado = esperado;
	}

	@Override
	public boolean esSatisfechaPor(final Object dato) {
		return Objects.equals(esperado, dato);
	}

}
