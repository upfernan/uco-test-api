package co.edu.uco.ucotestapi.transversal.especificacion.base;

public abstract class Especificacion<T> {

	public abstract boolean esSatisfechaPor(T dato);

	public Especificacion<T> y(final Especificacion<T> otra) {
		return new YEspecificacion<>(this, otra);
	}

	public Especificacion<T> o(final Especificacion<T> otra) {
		return new OEspecificacion<>(this, otra);
	}

	public Especificacion<T> no() {
		return new NoEspecificacion<>(this);
	}

}