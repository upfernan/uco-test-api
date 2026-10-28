package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.adaptadores;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.UsuarioEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios.UsuarioJPARepositorio;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.UsuarioRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.UsuarioEntidad;





@Component
public class UsuarioRepositorioAdapter implements UsuarioRepositorio {
	private final UsuarioJPARepositorio usuarioJPARepositorio;
	private final EntidadMapper<UsuarioEntidad, UsuarioEntidadJPA> mapper;

	public UsuarioRepositorioAdapter(UsuarioJPARepositorio usuarioJPARepositorio, EntidadMapper<UsuarioEntidad, UsuarioEntidadJPA> mapper) {
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