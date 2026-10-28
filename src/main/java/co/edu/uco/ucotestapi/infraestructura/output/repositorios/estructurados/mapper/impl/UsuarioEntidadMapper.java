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
		return new UsuarioEntidad(entidadJPA.getId(), entidadJPA.getCorreo(), entidadJPA.getClaveHash(),
				entidadJPA.getRol(), entidadJPA.getIdDocente(), entidadJPA.getIdEstudiante());
	}

	@Override
	public UsuarioEntidadJPA ConvertirAEntidadJPA(UsuarioEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de usuario no puede ser nula");
		}
		var entidadJPA = new UsuarioEntidadJPA();
		entidadJPA.setId(entidad.id());
		entidadJPA.setCorreo(entidad.correo());
		entidadJPA.setClaveHash(entidad.claveHash());
		entidadJPA.setRol(entidad.rol());
		entidadJPA.setIdDocente(UtilUUID.tieneValor(entidad.idDocente()) ? entidad.idDocente() : null);
		entidadJPA.setIdEstudiante(UtilUUID.tieneValor(entidad.idEstudiante()) ? entidad.idEstudiante() : null);
		return entidadJPA;
	}

}