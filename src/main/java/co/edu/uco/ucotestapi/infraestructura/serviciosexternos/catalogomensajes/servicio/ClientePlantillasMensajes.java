package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache.NombreCache;

/**
 * Pide al servicio externo del catalogo la plantilla de un mensaje por su codigo y la guarda en Redis.
 * Si el codigo no existe en el catalogo devuelve null (y no se guarda en la cache).
 */
public class ClientePlantillasMensajes {
	private final RestClient cliente;

	public ClientePlantillasMensajes(final RestClient.Builder constructor, final String url) {
		this.cliente = constructor.baseUrl(url).build();
	}

	@Cacheable(cacheNames = NombreCache.MENSAJES_CATALOGO, key = "#codigo", unless = "#result == null")
	public String consultarPlantilla(final String codigo) {
		try {
			var mensaje = cliente.get().uri("/api/v1/mensajes/{codigo}", codigo).retrieve().body(MensajeRemoto.class);
			return mensaje == null ? null : mensaje.contenido();
		} catch (HttpClientErrorException.NotFound noEncontrado) {
			return null;
		}
	}

	public record MensajeRemoto(String codigo, String contenido) {
	}

}
