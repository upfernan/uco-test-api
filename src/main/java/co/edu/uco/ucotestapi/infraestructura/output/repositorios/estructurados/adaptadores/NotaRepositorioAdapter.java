package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.adaptadores;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.NotaEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios.NotaJPARepositorio;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEntidad;


@Component
public class NotaRepositorioAdapter implements NotaRepositorio {
	private final NotaJPARepositorio notaJPARepositorio;
	private final EntidadMapper<NotaEntidad, NotaEntidadJPA> mapper;

	public NotaRepositorioAdapter(NotaJPARepositorio notaJPARepositorio, EntidadMapper<NotaEntidad, NotaEntidadJPA> mapper) {
		this.notaJPARepositorio = notaJPARepositorio;
		this.mapper = mapper;
	}

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

	@Override
	public void modificar(UUID id, NotaEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		notaJPARepositorio.save(entidadJPA);
	}

	@Override
	public void eliminar(UUID id) {
		notaJPARepositorio.deleteById(id);
	}

	@Override
	public List<NotaEntidad> consultarPorGrupo(UUID idGrupo) {
		return notaJPARepositorio.findByGrupo_Id(idGrupo).stream().map(mapper::ConvertirAEntidad).toList();
	}

}