package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.EstudianteGrupoEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.EstudianteEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.GrupoEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.GrupoEntidad;


@Component
public class EstudianteGrupoEntidadMapper implements EntidadMapper<EstudianteGrupoEntidad, EstudianteGrupoEntidadJPA> {
	private final EntidadMapper<EstudianteEntidad, EstudianteEntidadJPA> estudianteMapper;
	private final EntidadMapper<GrupoEntidad, GrupoEntidadJPA> grupoMapper;

	public EstudianteGrupoEntidadMapper(EntidadMapper<EstudianteEntidad, EstudianteEntidadJPA> estudianteMapper, EntidadMapper<GrupoEntidad, GrupoEntidadJPA> grupoMapper) {
		this.estudianteMapper = estudianteMapper;
		this.grupoMapper = grupoMapper;
	}

	@Override
	public EstudianteGrupoEntidad ConvertirAEntidad(EstudianteGrupoEntidadJPA entidadJPA) {
		if (entidadJPA == null) {
			return null;
		}
		var entidad = new EstudianteGrupoEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setEstudiante(estudianteMapper.ConvertirAEntidad(entidadJPA.getEstudiante()));
		entidad.setGrupo(grupoMapper.ConvertirAEntidad(entidadJPA.getGrupo()));
		return entidad;
	}

	@Override
	public EstudianteGrupoEntidadJPA ConvertirAEntidadJPA(EstudianteGrupoEntidad entidad) {
		if (entidad == null) {
			return null;
		}
		var entidadJPA = new EstudianteGrupoEntidadJPA();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setEstudiante(estudianteMapper.ConvertirAEntidadJPA(entidad.getEstudiante()));
		entidadJPA.setGrupo(grupoMapper.ConvertirAEntidadJPA(entidad.getGrupo()));
		return entidadJPA;
	}

}