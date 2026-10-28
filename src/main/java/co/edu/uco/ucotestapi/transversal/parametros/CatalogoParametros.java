package co.edu.uco.ucotestapi.transversal.parametros;

import co.edu.uco.ucotestapi.aplicacion.casouso.validador.Validador;

import java.util.regex.Pattern;

/**
 * Parametros de las reglas de negocio en un solo lugar, para no dejarlos quemados en cada validador.
 */
public final class CatalogoParametros {
	public static final int NOMBRE_LONGITUD_MINIMA = 2;
	public static final int NOMBRE_LONGITUD_MAXIMA = 100;
	public static final Pattern NOMBRE_PATRON = Pattern.compile("^[\\p{L} ]++$");
	public static final Pattern CORREO_PATRON = Pattern.compile("^[^\\s@]++@[^\\s@.]++(?:\\.[^\\s@.]++)++$");
	public static final int CLAVE_LONGITUD_MINIMA = 8;
	public static final Pattern CLAVE_TIENE_LETRA = Pattern.compile("\\p{L}");
	public static final Pattern CLAVE_TIENE_NUMERO = Pattern.compile("\\d");
	public static final int NOTA_VALOR_MINIMO = 0;
	public static final int NOTA_VALOR_MAXIMO = 500;
	public static final int NOTA_MAXIMO_DECIMALES = 2;

	private CatalogoParametros() {
		super();
	}

}