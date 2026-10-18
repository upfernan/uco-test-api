package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.DocenteEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.DocenteJPARepositorio;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.DocenteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;


@Component
public class DocenteRepositorioAdapter implements DocenteRepositorio {
	private final DocenteJPARepositorio docenteJPARepositorio;
	private final EntidadMapper<DocenteEntidad, DocenteEntidadJPA> mapper;

	public DocenteRepositorioAdapter(DocenteJPARepositorio docenteJPARepositorio, EntidadMapper<DocenteEntidad, DocenteEntidadJPA> mapper) {
		this.docenteJPARepositorio = docenteJPARepositorio;
		this.mapper = mapper;
	}

	@Override
	public void crear(DocenteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		docenteJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<DocenteEntidad> consultar(DocenteEntidad filtro) {
		var resultados = (filtro == null)
				? docenteJPARepositorio.findAll()
				: docenteJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)));
		return resultados.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<DocenteEntidad> consultarPorId(UUID id) {
		return docenteJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return docenteJPARepositorio.existsById(id);
	}

	@Override
	public void actualizar(DocenteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		docenteJPARepositorio.save(entidadJPA);
	}

	@Override
	public void eliminar(UUID id) {
		docenteJPARepositorio.deleteById(id);
	}

}