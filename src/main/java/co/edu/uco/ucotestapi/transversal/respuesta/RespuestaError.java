package co.edu.uco.ucotestapi.transversal.respuesta;

import java.time.LocalDateTime;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

public record RespuestaError(String mensaje, LocalDateTime fecha) {

	public static RespuestaError crear(final String mensaje) {
		return new RespuestaError(UtilTexto.aplicarTrim(mensaje), LocalDateTime.now());
	}

}