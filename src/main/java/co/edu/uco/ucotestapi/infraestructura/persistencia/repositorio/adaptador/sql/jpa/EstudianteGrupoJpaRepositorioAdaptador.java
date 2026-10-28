package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.EstudianteGrupoEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.EstudianteGrupoJpaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteGrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache.NombreCache;


@Component
public class EstudianteGrupoJpaRepositorioAdaptador implements EstudianteGrupoRepositorio {
	private final EstudianteGrupoJpaRepositorio estudianteGrupoJPARepositorio;
	private final MapeadorJpa<EstudianteGrupoEntidad, EstudianteGrupoEntidadJpa> mapper;

	public EstudianteGrupoJpaRepositorioAdaptador(EstudianteGrupoJpaRepositorio estudianteGrupoJPARepositorio, MapeadorJpa<EstudianteGrupoEntidad, EstudianteGrupoEntidadJpa> mapper) {
		this.estudianteGrupoJPARepositorio = estudianteGrupoJPARepositorio;
		this.mapper = mapper;
	}

	@CacheEvict(cacheNames = NombreCache.ESTUDIANTES_GRUPO, allEntries = true)
	@Override
	public void crear(EstudianteGrupoEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		estudianteGrupoJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<EstudianteGrupoEntidad> consultarTodos() {
		return estudianteGrupoJPARepositorio.findAll().stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public List<EstudianteGrupoEntidad> consultar(EstudianteGrupoEntidad filtro) {
		return estudianteGrupoJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)))
				.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<EstudianteGrupoEntidad> consultarPorId(UUID id) {
		return estudianteGrupoJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return estudianteGrupoJPARepositorio.existsById(id);
	}

	@Override
	public boolean existePorEstudianteYGrupo(UUID idEstudiante, UUID idGrupo) {
		return estudianteGrupoJPARepositorio.existsByEstudiante_IdAndGrupo_Id(idEstudiante, idGrupo);
	}

	@Override
	public Optional<EstudianteGrupoEntidad> consultarPorEstudianteYGrupo(UUID idEstudiante, UUID idGrupo) {
		return estudianteGrupoJPARepositorio.findByEstudiante_IdAndGrupo_Id(idEstudiante, idGrupo).map(mapper::ConvertirAEntidad);
	}

	@CacheEvict(cacheNames = NombreCache.ESTUDIANTES_GRUPO, allEntries = true)
	@Override
	public void modificar(UUID id, EstudianteGrupoEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		estudianteGrupoJPARepositorio.save(entidadJPA);
	}

	@CacheEvict(cacheNames = NombreCache.ESTUDIANTES_GRUPO, allEntries = true)
	@Override
	public void eliminar(UUID id) {
		estudianteGrupoJPARepositorio.deleteById(id);
	}

	@Override
	@Cacheable(cacheNames = NombreCache.ESTUDIANTES_GRUPO, key = "#idGrupo")
	public List<EstudianteGrupoEntidad> consultarPorGrupo(UUID idGrupo) {
		return estudianteGrupoJPARepositorio.findByGrupo_Id(idGrupo).stream().map(mapper::ConvertirAEntidad).toList();
	}

}