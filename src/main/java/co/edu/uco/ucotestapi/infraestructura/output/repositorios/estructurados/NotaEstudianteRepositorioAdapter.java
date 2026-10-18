package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.NotaEstudianteEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.NotaEstudianteJPARepositorio;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaEstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEstudianteEntidad;


@Component
public class NotaEstudianteRepositorioAdapter implements NotaEstudianteRepositorio {
	private final NotaEstudianteJPARepositorio notaEstudianteJPARepositorio;
	private final EntidadMapper<NotaEstudianteEntidad, NotaEstudianteEntidadJPA> mapper;

	public NotaEstudianteRepositorioAdapter(NotaEstudianteJPARepositorio notaEstudianteJPARepositorio, EntidadMapper<NotaEstudianteEntidad, NotaEstudianteEntidadJPA> mapper) {
		this.notaEstudianteJPARepositorio = notaEstudianteJPARepositorio;
		this.mapper = mapper;
	}

	@Override
	public void crear(NotaEstudianteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		notaEstudianteJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<NotaEstudianteEntidad> consultar(NotaEstudianteEntidad filtro) {
		var resultados = (filtro == null)
				? notaEstudianteJPARepositorio.findAll()
				: notaEstudianteJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)));
		return resultados.stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public Optional<NotaEstudianteEntidad> consultarPorId(UUID id) {
		return notaEstudianteJPARepositorio.findById(id).map(mapper::ConvertirAEntidad);
	}

	@Override
	public boolean existePorId(UUID id) {
		return notaEstudianteJPARepositorio.existsById(id);
	}

	@Override
	public boolean existePorEstudianteYNota(UUID idEstudiante, UUID idNota) {
		return notaEstudianteJPARepositorio.existsByEstudianteGrupo_Estudiante_IdAndNota_Id(idEstudiante, idNota);
	}

	@Override
	public void actualizar(NotaEstudianteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		notaEstudianteJPARepositorio.save(entidadJPA);
	}

	@Override
	public void eliminar(UUID id) {
		notaEstudianteJPARepositorio.deleteById(id);
	}

}