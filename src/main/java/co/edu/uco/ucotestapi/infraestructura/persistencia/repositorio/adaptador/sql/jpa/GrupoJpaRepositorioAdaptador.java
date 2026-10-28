package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.GrupoEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.GrupoJpaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.GrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache.NombreCache;


@Component
public class GrupoJpaRepositorioAdaptador implements GrupoRepositorio {
	private final GrupoJpaRepositorio grupoJPARepositorio;
	private final MapeadorJpa<GrupoEntidad, GrupoEntidadJpa> mapper;

	public GrupoJpaRepositorioAdaptador(GrupoJpaRepositorio grupoJPARepositorio, MapeadorJpa<GrupoEntidad, GrupoEntidadJpa> mapper) {
		this.grupoJPARepositorio = grupoJPARepositorio;
		this.mapper = mapper;
	}

	@CacheEvict(cacheNames = { NombreCache.GRUPOS_DOCENTE, NombreCache.ESTUDIANTES_GRUPO, NombreCache.NOTAS_GRUPO }, allEntries = true)
	@Override
	public void crear(GrupoEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		grupoJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<GrupoEntidad> consultarTodos() {
		return grupoJPARepositorio.findAll().stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public List<GrupoEntidad> consultar(GrupoEntidad filtro) {
		return grupoJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)))
				.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<GrupoEntidad> consultarPorId(UUID id) {
		return grupoJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return grupoJPARepositorio.existsById(id);
	}

	@CacheEvict(cacheNames = { NombreCache.GRUPOS_DOCENTE, NombreCache.ESTUDIANTES_GRUPO, NombreCache.NOTAS_GRUPO }, allEntries = true)
	@Override
	public void modificar(UUID id, GrupoEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		grupoJPARepositorio.save(entidadJPA);
	}

	@CacheEvict(cacheNames = { NombreCache.GRUPOS_DOCENTE, NombreCache.ESTUDIANTES_GRUPO, NombreCache.NOTAS_GRUPO }, allEntries = true)
	@Override
	public void eliminar(UUID id) {
		grupoJPARepositorio.deleteById(id);
	}

	@Override
	@Cacheable(cacheNames = NombreCache.GRUPOS_DOCENTE, key = "#idDocente")
	public List<GrupoEntidad> consultarPorDocente(UUID idDocente) {
		return grupoJPARepositorio.findByDocente_Id(idDocente).stream().map(mapper::ConvertirAEntidad).toList();
	}

}