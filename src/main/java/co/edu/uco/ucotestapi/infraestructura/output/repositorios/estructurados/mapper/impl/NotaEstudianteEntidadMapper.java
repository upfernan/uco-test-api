package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.NotaEstudianteEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.EstudianteGrupoEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.NotaEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEstudianteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEntidad;


@Component
public class NotaEstudianteEntidadMapper implements EntidadMapper<NotaEstudianteEntidad, NotaEstudianteEntidadJPA> {
	private final EntidadMapper<EstudianteGrupoEntidad, EstudianteGrupoEntidadJPA> estudianteGrupoMapper;
	private final EntidadMapper<NotaEntidad, NotaEntidadJPA> notaMapper;

	public NotaEstudianteEntidadMapper(EntidadMapper<EstudianteGrupoEntidad, EstudianteGrupoEntidadJPA> estudianteGrupoMapper, EntidadMapper<NotaEntidad, NotaEntidadJPA> notaMapper) {
		this.estudianteGrupoMapper = estudianteGrupoMapper;
		this.notaMapper = notaMapper;
	}

	@Override
	public NotaEstudianteEntidad ConvertirAEntidad(NotaEstudianteEntidadJPA entidadJPA) {
		if (entidadJPA == null) {
			return null;
		}
		var entidad = new NotaEstudianteEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setValor(entidadJPA.getValor());
		entidad.setEstudianteGrupo(estudianteGrupoMapper.ConvertirAEntidad(entidadJPA.getEstudianteGrupo()));
		entidad.setNota(notaMapper.ConvertirAEntidad(entidadJPA.getNota()));
		return entidad;
	}

	@Override
	public NotaEstudianteEntidadJPA ConvertirAEntidadJPA(NotaEstudianteEntidad entidad) {
		if (entidad == null) {
			return null;
		}
		var entidadJPA = new NotaEstudianteEntidadJPA();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setValor(entidad.getValor());
		entidadJPA.setEstudianteGrupo(estudianteGrupoMapper.ConvertirAEntidadJPA(entidad.getEstudianteGrupo()));
		entidadJPA.setNota(notaMapper.ConvertirAEntidadJPA(entidad.getNota()));
		return entidadJPA;
	}

}