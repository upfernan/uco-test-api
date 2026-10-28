package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.DocenteEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;


@Component
public class DocenteMapeadorJpa implements MapeadorJpa<DocenteEntidad, DocenteEntidadJpa> {

	@Override
	public DocenteEntidad ConvertirAEntidad(DocenteEntidadJpa entidadJPA) {
		if (entidadJPA == null) {
			return new DocenteEntidad();
		}
		var entidad = new DocenteEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setNombre(entidadJPA.getNombre());
		entidad.setCorreo(entidadJPA.getCorreo());
		entidad.setEstado(entidadJPA.getEstado());
		return entidad;
	}

	@Override
	public DocenteEntidadJpa ConvertirAEntidadJPA(DocenteEntidad entidad) {
		if (entidad == null) {
			return new DocenteEntidadJpa();
		}
		var entidadJPA = new DocenteEntidadJpa();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setNombre(entidad.getNombre());
		entidadJPA.setCorreo(entidad.getCorreo());
		entidadJPA.setEstado(entidad.getEstado());
		return entidadJPA;
	}

}