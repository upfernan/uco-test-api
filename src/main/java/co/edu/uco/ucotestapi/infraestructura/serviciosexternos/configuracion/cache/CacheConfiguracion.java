package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;
import tools.jackson.databind.json.JsonMapper;

/**
 * Cache de las consultas en Redis. Cada cache guarda una lista de entidades en JSON con su tipo exacto
 * y vence a los 10 minutos. Si Redis falla, la consulta va directo a la base en vez de responder error.
 */
@Configuration
@EnableCaching
public class CacheConfiguracion implements CachingConfigurer {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfiguracion.class);
	private static final Duration TIEMPO_VIDA = Duration.ofMinutes(10);
	private static final String PREFIJO = "uco-test-api::";

	@Bean
	public RedisCacheManagerBuilderCustomizer cachesRedis() {
		return constructor -> constructor
				.withCacheConfiguration(NombreCache.GRUPOS_DOCENTE, configuracion(GrupoEntidad.class))
				.withCacheConfiguration(NombreCache.ESTUDIANTES_GRUPO, configuracion(EstudianteGrupoEntidad.class))
				.withCacheConfiguration(NombreCache.NOTAS_GRUPO, configuracion(NotaEntidad.class));
	}

	private static RedisCacheConfiguration configuracion(final Class<?> tipo) {
		var mapeador = JsonMapper.builder().build();
		var tipoLista = mapeador.getTypeFactory().constructCollectionType(List.class, tipo);
		return RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(TIEMPO_VIDA)
				.disableCachingNullValues()
				.prefixCacheNameWith(PREFIJO)
				.serializeValuesWith(SerializationPair.fromSerializer(new JacksonJsonRedisSerializer<>(mapeador, tipoLista)));
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return new CacheErrorHandler() {

			@Override
			public void handleCacheGetError(final RuntimeException excepcion, final Cache cache, final Object llave) {
				avisar("leer", cache, excepcion);
			}

			@Override
			public void handleCachePutError(final RuntimeException excepcion, final Cache cache, final Object llave,
					final Object valor) {
				avisar("guardar", cache, excepcion);
			}

			@Override
			public void handleCacheEvictError(final RuntimeException excepcion, final Cache cache, final Object llave) {
				avisar("borrar", cache, excepcion);
			}

			@Override
			public void handleCacheClearError(final RuntimeException excepcion, final Cache cache) {
				avisar("limpiar", cache, excepcion);
			}
		};
	}

	private static void avisar(final String operacion, final Cache cache, final RuntimeException excepcion) {
		LOGGER.warn("No se pudo {} en la cache {}: {}", operacion, cache.getName(), excepcion.getMessage());
	}

}
