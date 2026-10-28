package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.DocenteEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.DocenteJpaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache.NombreCache;


@Component
public class DocenteJpaRepositorioAdaptador implements DocenteRepositorio {
	private final DocenteJpaRepositorio docenteJPARepositorio;
	private final MapeadorJpa<DocenteEntidad, DocenteEntidadJpa> mapper;

	public DocenteJpaRepositorioAdaptador(DocenteJpaRepositorio docenteJPARepositorio, MapeadorJpa<DocenteEntidad, DocenteEntidadJpa> mapper) {
		this.docenteJPARepositorio = docenteJPARepositorio;
		this.mapper = mapper;
	}

	@CacheEvict(cacheNames = NombreCache.GRUPOS_DOCENTE, allEntries = true)
	@Override
	public void crear(DocenteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		docenteJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<DocenteEntidad> consultarTodos() {
		return docenteJPARepositorio.findAll().stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public List<DocenteEntidad> consultar(DocenteEntidad filtro) {
		return docenteJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)))
				.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<DocenteEntidad> consultarPorId(UUID id) {
		return docenteJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return docenteJPARepositorio.existsById(id);
	}

	@CacheEvict(cacheNames = NombreCache.GRUPOS_DOCENTE, allEntries = true)
	@Override
	public void modificar(UUID id, DocenteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		docenteJPARepositorio.save(entidadJPA);
	}

	@CacheEvict(cacheNames = NombreCache.GRUPOS_DOCENTE, allEntries = true)
	@Override
	public void eliminar(UUID id) {
		docenteJPARepositorio.deleteById(id);
	}

	@Override
	public Optional<DocenteEntidad> consultarPorCorreo(String correo) {
		return docenteJPARepositorio.findByCorreo(correo).map(mapper::ConvertirAEntidad);
	}

}