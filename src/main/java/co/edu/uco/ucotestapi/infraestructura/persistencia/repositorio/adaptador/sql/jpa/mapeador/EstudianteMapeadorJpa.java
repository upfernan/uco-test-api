package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.EstudianteEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteEntidad;


@Component
public class EstudianteMapeadorJpa implements MapeadorJpa<EstudianteEntidad, EstudianteEntidadJpa> {

	@Override
	public EstudianteEntidad ConvertirAEntidad(EstudianteEntidadJpa entidadJPA) {
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
	public EstudianteEntidadJpa ConvertirAEntidadJPA(EstudianteEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de estudiante no puede ser nula");
		}
		var entidadJPA = new EstudianteEntidadJpa();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setNombre(entidad.getNombre());
		entidadJPA.setCorreo(entidad.getCorreo());
		return entidadJPA;
	}

}