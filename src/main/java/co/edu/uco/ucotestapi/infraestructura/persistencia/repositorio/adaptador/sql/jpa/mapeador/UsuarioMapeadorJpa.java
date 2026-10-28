package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.UsuarioEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;


@Component
public class UsuarioMapeadorJpa implements MapeadorJpa<UsuarioEntidad, UsuarioEntidadJpa> {

	@Override
	public UsuarioEntidad ConvertirAEntidad(UsuarioEntidadJpa entidadJPA) {
		if (entidadJPA == null) {
			throw new IllegalArgumentException("La entidad JPA de usuario no puede ser nula");
		}
		return new UsuarioEntidad(entidadJPA.getId(), entidadJPA.getCorreo(), entidadJPA.getClaveHash(),
				entidadJPA.getRol(), entidadJPA.getIdDocente(), entidadJPA.getIdEstudiante());
	}

	@Override
	public UsuarioEntidadJpa ConvertirAEntidadJPA(UsuarioEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de usuario no puede ser nula");
		}
		var entidadJPA = new UsuarioEntidadJpa();
		entidadJPA.setId(entidad.id());
		entidadJPA.setCorreo(entidad.correo());
		entidadJPA.setClaveHash(entidad.claveHash());
		entidadJPA.setRol(entidad.rol());
		entidadJPA.setIdDocente(UtilUUID.tieneValor(entidad.idDocente()) ? entidad.idDocente() : null);
		entidadJPA.setIdEstudiante(UtilUUID.tieneValor(entidad.idEstudiante()) ? entidad.idEstudiante() : null);
		return entidadJPA;
	}

}