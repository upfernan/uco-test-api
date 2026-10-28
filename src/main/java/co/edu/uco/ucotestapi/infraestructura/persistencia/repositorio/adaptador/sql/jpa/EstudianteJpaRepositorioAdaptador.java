package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.EstudianteEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.EstudianteJpaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache.NombreCache;





@Component
public class EstudianteJpaRepositorioAdaptador implements EstudianteRepositorio {
	private final EstudianteJpaRepositorio estudianteJPARepositorio;
	private final MapeadorJpa<EstudianteEntidad, EstudianteEntidadJpa> mapper;

	public EstudianteJpaRepositorioAdaptador(EstudianteJpaRepositorio estudianteJPARepositorio, MapeadorJpa<EstudianteEntidad, EstudianteEntidadJpa> mapper) {
		this.estudianteJPARepositorio = estudianteJPARepositorio;
		this.mapper = mapper;
	}

	@CacheEvict(cacheNames = NombreCache.ESTUDIANTES_GRUPO, allEntries = true)
	@Override
	public void crear(EstudianteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		estudianteJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<EstudianteEntidad> consultarTodos() {
		return estudianteJPARepositorio.findAll().stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public List<EstudianteEntidad> consultar(EstudianteEntidad filtro) {
		return estudianteJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)))
				.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<EstudianteEntidad> consultarPorId(UUID id) {
		return estudianteJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return estudianteJPARepositorio.existsById(id);
	}

	@CacheEvict(cacheNames = NombreCache.ESTUDIANTES_GRUPO, allEntries = true)
	@Override
	public void modificar(UUID id, EstudianteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		estudianteJPARepositorio.save(entidadJPA);
	}

	@CacheEvict(cacheNames = NombreCache.ESTUDIANTES_GRUPO, allEntries = true)
	@Override
	public void eliminar(UUID id) {
		estudianteJPARepositorio.deleteById(id);
	}

	@Override
	public Optional<EstudianteEntidad> consultarPorCorreo(String correo) {
		return estudianteJPARepositorio.findByCorreo(correo).map(mapper::ConvertirAEntidad);
	}

}