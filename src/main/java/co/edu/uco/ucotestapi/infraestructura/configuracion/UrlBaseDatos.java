package co.edu.uco.ucotestapi.infraestructura.configuracion;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Convierte la DATABASE_URL que entregan Neon o Render al formato que entiende JDBC.
 * Acepta cualquiera de los formatos que copia la consola de Neon: la cadena postgresql://..., con comillas,
 * con el prefijo "psql", en formato .env (DATABASE_URL='...'), en formato jdbc:postgresql://... o como
 * variables PGHOST, PGDATABASE, PGUSER y PGPASSWORD.
 * Los mensajes de error nunca incluyen el valor recibido, porque trae la clave.
 */
public record UrlBaseDatos(String jdbcUrl, String usuario, String clave) {

	private static final int PUERTO_POSTGRES = 5432;
	private static final String PARAMETRO_NO_SOPORTADO_POR_JDBC = "channel_binding";
	private static final String FORMATO_ESPERADO = "postgresql://usuario:clave@host/base?sslmode=require";
	private static final Pattern URL = Pattern.compile("(jdbc:)?postgres(ql)?://[^\\s'\"]+");
	private static final Pattern VARIABLE_PG = Pattern
			.compile("PG(HOST|PORT|DATABASE|USER|PASSWORD)\\s*=\\s*['\"]?([^'\"\\s]+)['\"]?");

	public static UrlBaseDatos desde(final String valor) {
		var texto = UtilTexto.aplicarTrim(valor);
		if (texto.isEmpty()) {
			throw new IllegalStateException("DATABASE_URL llegó vacía. Configure la cadena de conexión " + FORMATO_ESPERADO);
		}
		var url = URL.matcher(texto);
		if (url.find()) {
			return desdeUrl(url.group());
		}
		if (texto.contains("PGHOST")) {
			return desdeVariablesPg(texto);
		}
		throw new IllegalStateException("DATABASE_URL no tiene el formato esperado " + FORMATO_ESPERADO + ". " + describir(texto));
	}

	private static UrlBaseDatos desdeUrl(final String texto) {
		if (texto.startsWith("jdbc:")) {
			return new UrlBaseDatos(texto, UtilTexto.VACIO, UtilTexto.VACIO);
		}
		var uri = crearUri(texto);
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

	private static UrlBaseDatos desdeVariablesPg(final String texto) {
		var variables = new HashMap<String, String>();
		var coincidencia = VARIABLE_PG.matcher(texto);
		while (coincidencia.find()) {
			variables.put(coincidencia.group(1), coincidencia.group(2));
		}
		if (!variables.containsKey("HOST") || !variables.containsKey("USER") || !variables.containsKey("PASSWORD")) {
			throw new IllegalStateException("DATABASE_URL trae variables PG* incompletas: faltan PGHOST, PGUSER o PGPASSWORD");
		}
		var jdbcUrl = "jdbc:postgresql://" + variables.get("HOST") + ":"
				+ variables.getOrDefault("PORT", String.valueOf(PUERTO_POSTGRES)) + "/"
				+ variables.getOrDefault("DATABASE", "neondb") + "?sslmode=require";
		return new UrlBaseDatos(jdbcUrl, variables.get("USER"), variables.get("PASSWORD"));
	}

	private static String describir(final String texto) {
		return "Se recibieron " + texto.length() + " caracteres"
				+ (texto.contains("://") ? ", con '://' pero sin el esquema postgresql" : ", sin '://'")
				+ (texto.contains("@") ? ", con '@'" : ", sin '@'")
				+ (texto.contains("\n") ? ", con saltos de línea" : "") + ".";
	}

	private static URI crearUri(final String texto) {
		try {
			return new URI(texto);
		} catch (URISyntaxException _) {
			// No se encadena la excepcion: su mensaje incluye la cadena completa, con la clave.
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
