package co.edu.uco.ucotestapi.infraestructura.configuracion;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Convierte la DATABASE_URL que entregan Neon o Render al formato que entiende JDBC.
 * Acepta: postgresql://usuario:clave@host/base?sslmode=require, la misma con comillas o con el
 * prefijo "psql" (como la copia la consola de Neon) y el formato jdbc:postgresql://... ya listo.
 * Los mensajes de error nunca incluyen la cadena, porque trae la clave.
 */
public record UrlBaseDatos(String jdbcUrl, String usuario, String clave) {

	private static final int PUERTO_POSTGRES = 5432;
	private static final String PARAMETRO_NO_SOPORTADO_POR_JDBC = "channel_binding";
	private static final String FORMATO_ESPERADO = "postgresql://usuario:clave@host/base?sslmode=require";

	public static UrlBaseDatos desde(final String valor) {
		var limpio = limpiar(valor);
		if (limpio.startsWith("jdbc:postgresql://")) {
			return new UrlBaseDatos(limpio, UtilTexto.VACIO, UtilTexto.VACIO);
		}
		if (!limpio.startsWith("postgresql://") && !limpio.startsWith("postgres://")) {
			throw new IllegalStateException("DATABASE_URL no tiene el formato esperado " + FORMATO_ESPERADO);
		}
		var uri = crearUri(limpio);
		if (UtilTexto.esNula(uri.getHost()) || UtilTexto.esNula(uri.getRawUserInfo()) || !uri.getRawUserInfo().contains(":")) {
			throw new IllegalStateException("DATABASE_URL debe incluir usuario, clave y host: " + FORMATO_ESPERADO);
		}
		var credenciales = uri.getRawUserInfo().split(":", 2);
		var puerto = uri.getPort() == -1 ? PUERTO_POSTGRES : uri.getPort();
		var jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + puerto + UtilTexto.obtenerValorDefecto(uri.getRawPath())
				+ parametros(uri);
		return new UrlBaseDatos(jdbcUrl, decodificar(credenciales[0]), decodificar(credenciales[1]));
	}

	public boolean tieneCredenciales() {
		return UtilTexto.tieneContenido(usuario);
	}

	private static String limpiar(final String valor) {
		var texto = UtilTexto.aplicarTrim(valor);
		if (texto.startsWith("psql ")) {
			texto = texto.substring("psql ".length()).trim();
		}
		while (texto.length() >= 2 && (texto.startsWith("'") || texto.startsWith("\""))
				&& texto.charAt(texto.length() - 1) == texto.charAt(0)) {
			texto = texto.substring(1, texto.length() - 1).trim();
		}
		return texto;
	}

	private static URI crearUri(final String texto) {
		try {
			return new URI(texto);
		} catch (URISyntaxException excepcion) {
			throw new IllegalStateException("DATABASE_URL tiene caracteres no válidos. Formato esperado: " + FORMATO_ESPERADO);
		}
	}

	private static String parametros(final URI uri) {
		if (UtilTexto.esNula(uri.getRawQuery())) {
			return UtilTexto.VACIO;
		}
		var parametros = Arrays.stream(uri.getRawQuery().split("&"))
				.filter(parametro -> !parametro.startsWith(PARAMETRO_NO_SOPORTADO_POR_JDBC + "="))
				.collect(Collectors.joining("&"));
		return parametros.isEmpty() ? UtilTexto.VACIO : "?" + parametros;
	}

	private static String decodificar(final String valor) {
		return URLDecoder.decode(valor.replace("+", "%2B"), StandardCharsets.UTF_8);
	}

}
