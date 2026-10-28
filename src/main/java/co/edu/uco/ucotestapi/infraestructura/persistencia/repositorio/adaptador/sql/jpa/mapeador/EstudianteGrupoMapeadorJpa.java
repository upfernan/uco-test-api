package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.EstudianteGrupoEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.EstudianteEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.GrupoEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;


@Component
public class EstudianteGrupoMapeadorJpa implements MapeadorJpa<EstudianteGrupoEntidad, EstudianteGrupoEntidadJpa> {
	private final MapeadorJpa<EstudianteEntidad, EstudianteEntidadJpa> estudianteMapper;
	private final MapeadorJpa<GrupoEntidad, GrupoEntidadJpa> grupoMapper;

	public EstudianteGrupoMapeadorJpa(MapeadorJpa<EstudianteEntidad, EstudianteEntidadJpa> estudianteMapper, MapeadorJpa<GrupoEntidad, GrupoEntidadJpa> grupoMapper) {
		this.estudianteMapper = estudianteMapper;
		this.grupoMapper = grupoMapper;
	}

	@Override
	public EstudianteGrupoEntidad ConvertirAEntidad(EstudianteGrupoEntidadJpa entidadJPA) {
		if (entidadJPA == null) {
			return new EstudianteGrupoEntidad();
		}
		var entidad = new EstudianteGrupoEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setEstudiante(estudianteMapper.ConvertirAEntidad(entidadJPA.getEstudiante()));
		entidad.setGrupo(grupoMapper.ConvertirAEntidad(entidadJPA.getGrupo()));
		return entidad;
	}

	@Override
	public EstudianteGrupoEntidadJpa ConvertirAEntidadJPA(EstudianteGrupoEntidad entidad) {
		if (entidad == null) {
			return new EstudianteGrupoEntidadJpa();
		}
		var entidadJPA = new EstudianteGrupoEntidadJpa();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setEstudiante(estudianteMapper.ConvertirAEntidadJPA(entidad.getEstudiante()));
		entidadJPA.setGrupo(grupoMapper.ConvertirAEntidadJPA(entidad.getGrupo()));
		return entidadJPA;
	}

}