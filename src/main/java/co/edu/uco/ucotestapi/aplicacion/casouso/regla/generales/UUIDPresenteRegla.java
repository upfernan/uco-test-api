package co.edu.uco.ucotestapi.aplicacion.casouso.regla.generales;

import java.util.UUID;

import co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales.UUIDPresenteEspecificacion;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;

public final class UUIDPresenteRegla {
	private static final UUIDPresenteEspecificacion UUID_PRESENTE = new UUIDPresenteEspecificacion();

	private UUIDPresenteRegla() {
		super();
	}

	public static void ejecutarRegla(final UUID id, final CodigoMensaje codigoSiFalta) {
		CumpleEspecificacionRegla.ejecutarRegla(id, UUID_PRESENTE, codigoSiFalta);
	}

}