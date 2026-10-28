package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.impl;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.UsuarioEntidadJPA;
import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper.EntidadMapper;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.UsuarioEntidad;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;


@Component
public class UsuarioEntidadMapper implements EntidadMapper<UsuarioEntidad, UsuarioEntidadJPA> {

	@Override
	public UsuarioEntidad ConvertirAEntidad(UsuarioEntidadJPA entidadJPA) {
		if (entidadJPA == null) {
			throw new IllegalArgumentException("La entidad JPA de usuario no puede ser nula");
		}
		var entidad = new UsuarioEntidad();
		entidad.setId(entidadJPA.getId());
		entidad.setCorreo(entidadJPA.getCorreo());
		entidad.setClaveHash(entidadJPA.getClaveHash());
		entidad.setRol(entidadJPA.getRol());
		entidad.setIdDocente(entidadJPA.getIdDocente());
		entidad.setIdEstudiante(entidadJPA.getIdEstudiante());
		return entidad;
	}

	@Override
	public UsuarioEntidadJPA ConvertirAEntidadJPA(UsuarioEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de usuario no puede ser nula");
		}
		var entidadJPA = new UsuarioEntidadJPA();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setCorreo(entidad.getCorreo());
		entidadJPA.setClaveHash(entidad.getClaveHash());
		entidadJPA.setRol(entidad.getRol());
		entidadJPA.setIdDocente(UtilUUID.tieneValor(entidad.getIdDocente()) ? entidad.getIdDocente() : null);
		entidadJPA.setIdEstudiante(UtilUUID.tieneValor(entidad.getIdEstudiante()) ? entidad.getIdEstudiante() : null);
		return entidadJPA;
	}

}