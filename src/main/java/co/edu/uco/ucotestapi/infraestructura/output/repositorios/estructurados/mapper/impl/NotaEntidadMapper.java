package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.NotaEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.GrupoEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.GrupoEntidad;


@Component
public class NotaEntidadMapper implements EntidadMapper<NotaEntidad, NotaEntidadJPA> {
	private final EntidadMapper<GrupoEntidad, GrupoEntidadJPA> grupoMapper;

	public NotaEntidadMapper(EntidadMapper<GrupoEntidad, GrupoEntidadJPA> grupoMapper) {
		this.grupoMapper = grupoMapper;
	}

	@Override
	public NotaEntidad ConvertirAEntidad(NotaEntidadJPA entidadJPA) {
		if (entidadJPA == null) {
			throw new IllegalArgumentException("La entidad JPA de nota no puede ser nula");
		}
		var entidad = new NotaEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setFechaRegistroDesde(entidadJPA.getFechaRegistroDesde());
		entidad.setFechaRegistroHasta(entidadJPA.getFechaRegistroHasta());
		entidad.setEstado(entidadJPA.getEstado());
		entidad.setGrupo(grupoMapper.ConvertirAEntidad(entidadJPA.getGrupo()));
		return entidad;
	}

	@Override
	public NotaEntidadJPA ConvertirAEntidadJPA(NotaEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de nota no puede ser nula");
		}
		var entidadJPA = new NotaEntidadJPA();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setFechaRegistroDesde(entidad.getFechaRegistroDesde());
		entidadJPA.setFechaRegistroHasta(entidad.getFechaRegistroHasta());
		entidadJPA.setEstado(entidad.getEstado());
		entidadJPA.setGrupo(grupoMapper.ConvertirAEntidadJPA(entidad.getGrupo()));
		return entidadJPA;
	}

}