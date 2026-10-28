package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import java.util.regex.Pattern;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class ContieneTextoEspecificacion extends Especificacion<String> {
	private final Pattern patron;

	public ContieneTextoEspecificacion(final Pattern patron) {
		this.patron = patron;
	}

	@Override
	public boolean esSatisfechaPor(final String dato) {
		return patron.matcher(UtilTexto.obtenerValorDefecto(dato)).find();
	}

}
