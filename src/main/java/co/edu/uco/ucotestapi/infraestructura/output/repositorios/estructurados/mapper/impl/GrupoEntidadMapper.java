package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.GrupoEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.DocenteEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.GrupoEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;


@Component
public class GrupoEntidadMapper implements EntidadMapper<GrupoEntidad, GrupoEntidadJPA> {
	private final EntidadMapper<DocenteEntidad, DocenteEntidadJPA> docenteMapper;

	public GrupoEntidadMapper(EntidadMapper<DocenteEntidad, DocenteEntidadJPA> docenteMapper) {
		this.docenteMapper = docenteMapper;
	}

	@Override
	public GrupoEntidad ConvertirAEntidad(GrupoEntidadJPA entidadJPA) {
		if (entidadJPA == null) {
			throw new IllegalArgumentException("La entidad JPA de grupo no puede ser nula");
		}
		var entidad = new GrupoEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setNombre(entidadJPA.getNombre());
		entidad.setEstado(entidadJPA.getEstado());
		entidad.setFechaInicio(entidadJPA.getFechaInicio());
		entidad.setFechaFin(entidadJPA.getFechaFin());
		entidad.setDocente(docenteMapper.ConvertirAEntidad(entidadJPA.getDocente()));
		return entidad;
	}

	@Override
	public GrupoEntidadJPA ConvertirAEntidadJPA(GrupoEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de grupo no puede ser nula");
		}
		var entidadJPA = new GrupoEntidadJPA();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setNombre(entidad.getNombre());
		entidadJPA.setEstado(entidad.getEstado());
		entidadJPA.setFechaInicio(entidad.getFechaInicio());
		entidadJPA.setFechaFin(entidad.getFechaFin());
		entidadJPA.setDocente(docenteMapper.ConvertirAEntidadJPA(entidad.getDocente()));
		return entidadJPA;
	}

}