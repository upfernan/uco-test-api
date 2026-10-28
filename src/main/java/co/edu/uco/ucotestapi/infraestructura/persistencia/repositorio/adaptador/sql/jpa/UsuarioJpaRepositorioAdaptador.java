package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.UsuarioEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.UsuarioJpaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;





@Component
public class UsuarioJpaRepositorioAdaptador implements UsuarioRepositorio {
	private final UsuarioJpaRepositorio usuarioJPARepositorio;
	private final MapeadorJpa<UsuarioEntidad, UsuarioEntidadJpa> mapper;

	public UsuarioJpaRepositorioAdaptador(UsuarioJpaRepositorio usuarioJPARepositorio, MapeadorJpa<UsuarioEntidad, UsuarioEntidadJpa> mapper) {
		this.usuarioJPARepositorio = usuarioJPARepositorio;
		this.mapper = mapper;
	}

	@Override
	public void crear(UsuarioEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		usuarioJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<UsuarioEntidad> consultarTodos() {
		return usuarioJPARepositorio.findAll().stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public List<UsuarioEntidad> consultar(UsuarioEntidad filtro) {
		return usuarioJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)))
				.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<UsuarioEntidad> consultarPorId(UUID id) {
		return usuarioJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return usuarioJPARepositorio.existsById(id);
	}

	@Override
	public void modificar(UUID id, UsuarioEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		usuarioJPARepositorio.save(entidadJPA);
	}

	@Override
	public void eliminar(UUID id) {
		usuarioJPARepositorio.deleteById(id);
	}


	@Override
	public boolean existePorCorreo(String correo) {
		return usuarioJPARepositorio.existsByCorreo(correo);
	}

	@Override
	public Optional<UsuarioEntidad> consultarPorCorreo(String correo) {
		return usuarioJPARepositorio.findByCorreo(correo).map(mapper::ConvertirAEntidad);
	}

}