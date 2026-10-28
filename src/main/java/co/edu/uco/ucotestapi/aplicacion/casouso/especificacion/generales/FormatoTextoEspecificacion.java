package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import java.util.regex.Pattern;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class FormatoTextoEspecificacion extends Especificacion<String> {
	private final Pattern patron;

	public FormatoTextoEspecificacion(final Pattern patron) {
		this.patron = patron;
	}

	@Override
	public boolean esSatisfechaPor(final String dato) {
		return patron.matcher(UtilTexto.aplicarTrim(dato)).matches();
	}

}
