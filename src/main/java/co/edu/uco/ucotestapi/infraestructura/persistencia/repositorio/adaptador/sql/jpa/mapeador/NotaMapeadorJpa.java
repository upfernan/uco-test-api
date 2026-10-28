package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.NotaEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.GrupoEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;


@Component
public class NotaMapeadorJpa implements MapeadorJpa<NotaEntidad, NotaEntidadJpa> {
	private final MapeadorJpa<GrupoEntidad, GrupoEntidadJpa> grupoMapper;

	public NotaMapeadorJpa(MapeadorJpa<GrupoEntidad, GrupoEntidadJpa> grupoMapper) {
		this.grupoMapper = grupoMapper;
	}

	@Override
	public NotaEntidad ConvertirAEntidad(NotaEntidadJpa entidadJPA) {
		if (entidadJPA == null) {
			return new NotaEntidad();
		}
		var entidad = new NotaEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setNombre(entidadJPA.getNombre());
		entidad.setFechaRegistroDesde(entidadJPA.getFechaRegistroDesde());
		entidad.setFechaRegistroHasta(entidadJPA.getFechaRegistroHasta());
		entidad.setEstado(entidadJPA.getEstado());
		entidad.setGrupo(grupoMapper.ConvertirAEntidad(entidadJPA.getGrupo()));
		return entidad;
	}

	@Override
	public NotaEntidadJpa ConvertirAEntidadJPA(NotaEntidad entidad) {
		if (entidad == null) {
			return new NotaEntidadJpa();
		}
		var entidadJPA = new NotaEntidadJpa();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setNombre(entidad.getNombre());
		entidadJPA.setFechaRegistroDesde(entidad.getFechaRegistroDesde());
		entidadJPA.setFechaRegistroHasta(entidad.getFechaRegistroHasta());
		entidadJPA.setEstado(entidad.getEstado());
		entidadJPA.setGrupo(grupoMapper.ConvertirAEntidadJPA(entidad.getGrupo()));
		return entidadJPA;
	}

}