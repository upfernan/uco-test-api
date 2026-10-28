package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class TextoPresenteEspecificacion extends Especificacion<String> {

	@Override
	public boolean esSatisfechaPor(final String dato) {
		return UtilTexto.tieneContenido(dato);
	}

}
