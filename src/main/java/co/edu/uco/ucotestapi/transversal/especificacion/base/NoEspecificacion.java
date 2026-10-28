package co.edu.uco.ucotestapi.transversal.especificacion.base;

public final class NoEspecificacion<T> extends Especificacion<T> {
	private final Especificacion<T> especificacion;

	public NoEspecificacion(final Especificacion<T> especificacion) {
		this.especificacion = especificacion;
	}

	@Override
	public boolean esSatisfechaPor(final T dato) {
		return !especificacion.esSatisfechaPor(dato);
	}

}
