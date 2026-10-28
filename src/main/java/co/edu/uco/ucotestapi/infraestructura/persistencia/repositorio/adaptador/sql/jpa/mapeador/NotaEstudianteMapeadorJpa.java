package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.NotaEstudianteEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.EstudianteGrupoEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.NotaEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEstudianteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;


@Component
public class NotaEstudianteMapeadorJpa implements MapeadorJpa<NotaEstudianteEntidad, NotaEstudianteEntidadJpa> {
	private final MapeadorJpa<EstudianteGrupoEntidad, EstudianteGrupoEntidadJpa> estudianteGrupoMapper;
	private final MapeadorJpa<NotaEntidad, NotaEntidadJpa> notaMapper;

	public NotaEstudianteMapeadorJpa(MapeadorJpa<EstudianteGrupoEntidad, EstudianteGrupoEntidadJpa> estudianteGrupoMapper, MapeadorJpa<NotaEntidad, NotaEntidadJpa> notaMapper) {
		this.estudianteGrupoMapper = estudianteGrupoMapper;
		this.notaMapper = notaMapper;
	}

	@Override
	public NotaEstudianteEntidad ConvertirAEntidad(NotaEstudianteEntidadJpa entidadJPA) {
		if (entidadJPA == null) {
			throw new IllegalArgumentException("La entidad JPA de nota del estudiante no puede ser nula");
		}
		var entidad = new NotaEstudianteEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setValor(entidadJPA.getValor());
		entidad.setEstudianteGrupo(estudianteGrupoMapper.ConvertirAEntidad(entidadJPA.getEstudianteGrupo()));
		entidad.setNota(notaMapper.ConvertirAEntidad(entidadJPA.getNota()));
		return entidad;
	}

	@Override
	public NotaEstudianteEntidadJpa ConvertirAEntidadJPA(NotaEstudianteEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de nota del estudiante no puede ser nula");
		}
		var entidadJPA = new NotaEstudianteEntidadJpa();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setValor(entidad.getValor());
		entidadJPA.setEstudianteGrupo(estudianteGrupoMapper.ConvertirAEntidadJPA(entidad.getEstudianteGrupo()));
		entidadJPA.setNota(notaMapper.ConvertirAEntidadJPA(entidad.getNota()));
		return entidadJPA;
	}

}