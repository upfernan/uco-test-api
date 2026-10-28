package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.NotaEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.NotaJpaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache.NombreCache;


@Component
public class NotaJpaRepositorioAdaptador implements NotaRepositorio {
	private final NotaJpaRepositorio notaJPARepositorio;
	private final MapeadorJpa<NotaEntidad, NotaEntidadJpa> mapper;

	public NotaJpaRepositorioAdaptador(NotaJpaRepositorio notaJPARepositorio, MapeadorJpa<NotaEntidad, NotaEntidadJpa> mapper) {
		this.notaJPARepositorio = notaJPARepositorio;
		this.mapper = mapper;
	}

	@CacheEvict(cacheNames = NombreCache.NOTAS_GRUPO, allEntries = true)
	@Override
	public void crear(NotaEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		notaJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<NotaEntidad> consultarTodos() {
		return notaJPARepositorio.findAll().stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public List<NotaEntidad> consultar(NotaEntidad filtro) {
		return notaJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)))
				.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<NotaEntidad> consultarPorId(UUID id) {
		return notaJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return notaJPARepositorio.existsById(id);
	}

	@CacheEvict(cacheNames = NombreCache.NOTAS_GRUPO, allEntries = true)
	@Override
	public void modificar(UUID id, NotaEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		notaJPARepositorio.save(entidadJPA);
	}

	@CacheEvict(cacheNames = NombreCache.NOTAS_GRUPO, allEntries = true)
	@Override
	public void eliminar(UUID id) {
		notaJPARepositorio.deleteById(id);
	}

	@Override
	@Cacheable(cacheNames = NombreCache.NOTAS_GRUPO, key = "#idGrupo")
	public List<NotaEntidad> consultarPorGrupo(UUID idGrupo) {
		return notaJPARepositorio.findByGrupo_Id(idGrupo).stream().map(mapper::ConvertirAEntidad).toList();
	}

}