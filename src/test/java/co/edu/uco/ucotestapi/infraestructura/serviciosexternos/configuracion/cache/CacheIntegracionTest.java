package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteGrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.GrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;
import co.edu.uco.ucotestapi.inicializador.UcoTestApiApplication;

/**
 * Prueba la cache contra el Redis de docker-compose (o el servicio de Redis del CI) con los datos demo de V2 y V3.
 * Spring Data Redis guarda y limpia en segundo plano, por eso las verificaciones esperan a que Redis termine.
 */
@SpringBootTest(classes = UcoTestApiApplication.class)
class CacheIntegracionTest {
	private static final UUID DOCENTE_DEMO = UUID.fromString("11111111-1111-1111-1111-111111111111");
	private static final UUID GRUPO_DEMO = UUID.fromString("33333333-3333-3333-3333-111111111111");
	private static final Duration ESPERA_MAXIMA = Duration.ofSeconds(5);

	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private GrupoRepositorio grupoRepositorio;
	@Autowired
	private EstudianteGrupoRepositorio estudianteGrupoRepositorio;
	@Autowired
	private NotaRepositorio notaRepositorio;

	@BeforeEach
	void limpiarCaches() {
		cacheManager.getCacheNames().forEach(nombre -> cacheManager.getCache(nombre).invalidate());
	}

	@Test
	void usaRedisComoCache() {
		assertInstanceOf(RedisCacheManager.class, cacheManager);
	}

	@Test
	void guardaLosGruposDelDocenteYLosLeeConSuTipo() {
		var consultados = grupoRepositorio.consultarPorDocente(DOCENTE_DEMO);

		var enCache = this.<GrupoEntidad>esperarEnCache(NombreCache.GRUPOS_DOCENTE, DOCENTE_DEMO);
		assertNotNull(enCache);
		assertFalse(enCache.isEmpty());
		assertEquals(consultados.get(0).getNombre(), enCache.get(0).getNombre());
		assertEquals(consultados.get(0).getFechaInicio(), enCache.get(0).getFechaInicio());
		assertEquals(consultados.get(0).getDocente().getId(), enCache.get(0).getDocente().getId());
		assertEquals(consultados.get(0).getNombre(), grupoRepositorio.consultarPorDocente(DOCENTE_DEMO).get(0).getNombre());
	}

	@Test
	void guardaLosEstudiantesYLasNotasDelGrupo() {
		var estudiantes = estudianteGrupoRepositorio.consultarPorGrupo(GRUPO_DEMO);
		var notas = notaRepositorio.consultarPorGrupo(GRUPO_DEMO);

		List<EstudianteGrupoEntidad> estudiantesEnCache = esperarEnCache(NombreCache.ESTUDIANTES_GRUPO, GRUPO_DEMO);
		List<NotaEntidad> notasEnCache = esperarEnCache(NombreCache.NOTAS_GRUPO, GRUPO_DEMO);
		assertEquals(estudiantes.get(0).getEstudiante().getId(), estudiantesEnCache.get(0).getEstudiante().getId());
		assertEquals(notas.size(), notasEnCache.size());
		assertEquals(notas.get(0).getFechaRegistroHasta(), notasEnCache.get(0).getFechaRegistroHasta());
	}

	@Test
	void cambiarUnGrupoLimpiaLasCachesQueLoIncluyen() {
		grupoRepositorio.consultarPorDocente(DOCENTE_DEMO);
		notaRepositorio.consultarPorGrupo(GRUPO_DEMO);
		esperarEnCache(NombreCache.GRUPOS_DOCENTE, DOCENTE_DEMO);
		esperarEnCache(NombreCache.NOTAS_GRUPO, GRUPO_DEMO);

		grupoRepositorio.eliminar(UUID.randomUUID());

		await().atMost(ESPERA_MAXIMA).untilAsserted(() -> {
			assertNull(cache(NombreCache.GRUPOS_DOCENTE).get(DOCENTE_DEMO));
			assertNull(cache(NombreCache.NOTAS_GRUPO).get(GRUPO_DEMO));
		});
	}

	private Cache cache(final String nombre) {
		return cacheManager.getCache(nombre);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> esperarEnCache(final String nombre, final UUID llave) {
		var valor = await().atMost(ESPERA_MAXIMA).until(() -> cache(nombre).get(llave), Objects::nonNull);
		return (List<T>) valor.get();
	}

}
