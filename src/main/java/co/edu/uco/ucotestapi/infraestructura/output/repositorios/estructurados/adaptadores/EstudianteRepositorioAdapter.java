package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.adaptadores;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.EstudianteEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios.EstudianteJPARepositorio;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.EstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteEntidad;





@Component
public class EstudianteRepositorioAdapter implements EstudianteRepositorio {
	private final EstudianteJPARepositorio estudianteJPARepositorio;
	private final EntidadMapper<EstudianteEntidad, EstudianteEntidadJPA> mapper;

	public EstudianteRepositorioAdapter(EstudianteJPARepositorio estudianteJPARepositorio, EntidadMapper<EstudianteEntidad, EstudianteEntidadJPA> mapper) {
		this.estudianteJPARepositorio = estudianteJPARepositorio;
		this.mapper = mapper;
	}

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

	@Override
	public void modificar(UUID id, EstudianteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		estudianteJPARepositorio.save(entidadJPA);
	}

	@Override
	public void eliminar(UUID id) {
		estudianteJPARepositorio.deleteById(id);
	}

	@Override
	public Optional<EstudianteEntidad> consultarPorCorreo(String correo) {
		return estudianteJPARepositorio.findByCorreo(correo).map(mapper::ConvertirAEntidad);
	}

}