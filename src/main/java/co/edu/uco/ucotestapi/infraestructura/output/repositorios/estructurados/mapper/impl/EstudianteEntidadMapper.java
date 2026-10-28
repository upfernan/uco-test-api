package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.EstudianteEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteEntidad;


@Component
public class EstudianteEntidadMapper implements EntidadMapper<EstudianteEntidad, EstudianteEntidadJPA> {

	@Override
	public EstudianteEntidad ConvertirAEntidad(EstudianteEntidadJPA entidadJPA) {
		// Recomendacion: usar un datamapper o MapStruct
		if (entidadJPA == null) {
			throw new IllegalArgumentException("La entidad JPA de estudiante no puede ser nula");
		}
		var entidad = new EstudianteEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setNombre(entidadJPA.getNombre());
		entidad.setCorreo(entidadJPA.getCorreo());
		return entidad;
	}

	@Override
	public EstudianteEntidadJPA ConvertirAEntidadJPA(EstudianteEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de estudiante no puede ser nula");
		}
		var entidadJPA = new EstudianteEntidadJPA();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setNombre(entidad.getNombre());
		entidadJPA.setCorreo(entidad.getCorreo());
		return entidadJPA;
	}

}