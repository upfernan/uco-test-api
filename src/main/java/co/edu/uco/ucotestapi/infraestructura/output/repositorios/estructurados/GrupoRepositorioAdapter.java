package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.GrupoEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.GrupoJPARepositorio;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.GrupoEntidad;


@Component
public class GrupoRepositorioAdapter implements GrupoRepositorio {
	private final GrupoJPARepositorio grupoJPARepositorio;
	private final EntidadMapper<GrupoEntidad, GrupoEntidadJPA> mapper;

	public GrupoRepositorioAdapter(GrupoJPARepositorio grupoJPARepositorio, EntidadMapper<GrupoEntidad, GrupoEntidadJPA> mapper) {
		this.grupoJPARepositorio = grupoJPARepositorio;
		this.mapper = mapper;
	}

	@Override
	public void crear(GrupoEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		grupoJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<GrupoEntidad> consultar(GrupoEntidad filtro) {
		var resultados = (filtro == null)
				? grupoJPARepositorio.findAll()
				: grupoJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)));
		return resultados.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<GrupoEntidad> consultarPorId(UUID id) {
		return grupoJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return grupoJPARepositorio.existsById(id);
	}

	@Override
	public void actualizar(GrupoEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		grupoJPARepositorio.save(entidadJPA);
	}

	@Override
	public void eliminar(UUID id) {
		grupoJPARepositorio.deleteById(id);
	}

}