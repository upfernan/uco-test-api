package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.DocenteEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;


@Component
public class DocenteEntidadMapper implements EntidadMapper<DocenteEntidad, DocenteEntidadJPA> {

	@Override
	public DocenteEntidad ConvertirAEntidad(DocenteEntidadJPA entidadJPA) {
		if (entidadJPA == null) {
			return null;
		}
		var entidad = new DocenteEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setEstado(entidadJPA.getEstado());
		return entidad;
	}

	@Override
	public DocenteEntidadJPA ConvertirAEntidadJPA(DocenteEntidad entidad) {
		if (entidad == null) {
			return null;
		}
		var entidadJPA = new DocenteEntidadJPA();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setEstado(entidad.getEstado());
		return entidadJPA;
	}

}