package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;

class CacheConfiguracionTest {

	@Test
	void unErrorDeRedisNoDetieneLaConsulta() {
		var manejador = new CacheConfiguracion().errorHandler();
		var cache = mock(Cache.class);
		when(cache.getName()).thenReturn(NombreCache.GRUPOS_DOCENTE);
		var error = new IllegalStateException("Redis no responde");

		assertDoesNotThrow(() -> {
			manejador.handleCacheGetError(error, cache, "llave");
			manejador.handleCachePutError(error, cache, "llave", "valor");
			manejador.handleCacheEvictError(error, cache, "llave");
			manejador.handleCacheClearError(error, cache);
		});
	}

}
