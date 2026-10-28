package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.NotaEstudianteEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.NotaEstudianteJpaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaEstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEstudianteEntidad;


@Component
public class NotaEstudianteJpaRepositorioAdaptador implements NotaEstudianteRepositorio {
	private final NotaEstudianteJpaRepositorio notaEstudianteJPARepositorio;
	private final MapeadorJpa<NotaEstudianteEntidad, NotaEstudianteEntidadJpa> mapper;

	public NotaEstudianteJpaRepositorioAdaptador(NotaEstudianteJpaRepositorio notaEstudianteJPARepositorio, MapeadorJpa<NotaEstudianteEntidad, NotaEstudianteEntidadJpa> mapper) {
		this.notaEstudianteJPARepositorio = notaEstudianteJPARepositorio;
		this.mapper = mapper;
	}

	@Override
	public void crear(NotaEstudianteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		notaEstudianteJPARepositorio.save(entidadJPA);
	}

	@Override
	public List<NotaEstudianteEntidad> consultarTodos() {
		return notaEstudianteJPARepositorio.findAll().stream().map(mapper::ConvertirAEntidad).toList();
	}

	@Override
	public List<NotaEstudianteEntidad> consultar(NotaEstudianteEntidad filtro) {
		return notaEstudianteJPARepositorio.findAll(Example.of(mapper.ConvertirAEntidadJPA(filtro)))
				.stream().map(mapper::ConvertirAEntidad).toList();
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
	public void modificar(UUID id, NotaEstudianteEntidad entidad) {
		var entidadJPA = mapper.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		notaEstudianteJPARepositorio.save(entidadJPA);
	}

	@Override
	public void eliminar(UUID id) {
		notaEstudianteJPARepositorio.deleteById(id);
	}

}