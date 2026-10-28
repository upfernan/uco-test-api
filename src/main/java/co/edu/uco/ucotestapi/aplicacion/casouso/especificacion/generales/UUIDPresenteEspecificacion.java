package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import java.util.UUID;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;
import co.edu.uco.ucotestapi.transversal.especificacion.base.Especificacion;

public final class UUIDPresenteEspecificacion extends Especificacion<UUID> {

	@Override
	public boolean esSatisfechaPor(final UUID dato) {
		return UtilUUID.tieneValor(dato);
	}

}
