package co.edu.uco.ucotestcatalogo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Los mensajes del catalogo: se cargan una vez al arrancar desde mensajes.properties.
 * Cada mensaje es una plantilla; {0}, {1}... los reemplaza quien lo pide.
 */
@Component
public class CatalogoMensajes {
	private final Map<String, String> mensajes;

	public CatalogoMensajes() {
		this.mensajes = cargar("mensajes.properties");
	}

	public Optional<String> consultar(final String codigo) {
		return Optional.ofNullable(mensajes.get(codigo));
	}

	public Map<String, String> consultarTodos() {
		return mensajes;
	}

	private static Map<String, String> cargar(final String archivo) {
		var propiedades = new Properties();
		try (var lector = new InputStreamReader(new ClassPathResource(archivo).getInputStream(), StandardCharsets.UTF_8)) {
			propiedades.load(lector);
		} catch (IOException excepcion) {
			throw new IllegalStateException("No se pudo leer " + archivo, excepcion);
		}
		var cargados = new TreeMap<String, String>();
		propiedades.stringPropertyNames().forEach(codigo -> cargados.put(codigo, propiedades.getProperty(codigo)));
		return Map.copyOf(cargados);
	}

}
