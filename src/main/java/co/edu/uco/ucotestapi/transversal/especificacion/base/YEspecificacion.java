package co.edu.uco.ucotestapi.transversal.especificacion.base;

public final class YEspecificacion<T> extends Especificacion<T> {
	private final Especificacion<T> izquierda;
	private final Especificacion<T> derecha;

	public YEspecificacion(final Especificacion<T> izquierda, final Especificacion<T> derecha) {
		this.izquierda = izquierda;
		this.derecha = derecha;
	}

	@Override
	public boolean esSatisfechaPor(final T dato) {
		return izquierda.esSatisfechaPor(dato) && derecha.esSatisfechaPor(dato);
	}

}
