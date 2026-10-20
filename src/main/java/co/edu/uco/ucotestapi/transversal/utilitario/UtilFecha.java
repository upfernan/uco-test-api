package co.edu.uco.ucotestapi.transversal.utilitario;

import java.time.Instant;
import java.time.LocalDate;

public final class UtilFecha {

	public static final LocalDate FECHA_DEFECTO = LocalDate.of(1900, 1, 1);
	public static final Instant INSTANTE_DEFECTO = Instant.EPOCH;

	private UtilFecha() {
		super();
	}

	public static boolean esNula(final LocalDate fecha) {
		return UtilObjeto.esNulo(fecha);
	}

	public static LocalDate obtenerValorDefecto(final LocalDate fecha, final LocalDate valorDefecto) {
		return UtilObjeto.obtenerValorDefecto(fecha, valorDefecto);
	}

	public static LocalDate obtenerValorDefecto(final LocalDate fecha) {
		return obtenerValorDefecto(fecha, FECHA_DEFECTO);
	}

	public static boolean esValorDefecto(final LocalDate fecha) {
		return FECHA_DEFECTO.equals(obtenerValorDefecto(fecha));
	}

	public static boolean esNulo(final Instant instante) {
		return UtilObjeto.esNulo(instante);
	}

	public static Instant obtenerValorDefecto(final Instant instante, final Instant valorDefecto) {
		return UtilObjeto.obtenerValorDefecto(instante, valorDefecto);
	}

	public static Instant obtenerValorDefecto(final Instant instante) {
		return obtenerValorDefecto(instante, INSTANTE_DEFECTO);
	}

	public static boolean esValorDefecto(final Instant instante) {
		return INSTANTE_DEFECTO.equals(obtenerValorDefecto(instante));
	}

}