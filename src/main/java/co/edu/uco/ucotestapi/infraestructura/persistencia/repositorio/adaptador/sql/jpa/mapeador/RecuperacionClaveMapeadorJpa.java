package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.RecuperacionClaveEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.RecuperacionClaveEntidadJpa;

@Component
public class RecuperacionClaveMapeadorJpa implements MapeadorJpa<RecuperacionClaveEntidad, RecuperacionClaveEntidadJpa> {

	@Override
	public RecuperacionClaveEntidad ConvertirAEntidad(RecuperacionClaveEntidadJpa entidadJPA) {
		var entidad = new RecuperacionClaveEntidad();
		if (entidadJPA == null) {
			return entidad;
		}
		entidad.setId(entidadJPA.getId());
		entidad.setIdUsuario(entidadJPA.getIdUsuario());
		entidad.setCodigoHash(entidadJPA.getCodigoHash());
		entidad.setExpiraEn(entidadJPA.getExpiraEn());
		entidad.setIntentos(entidadJPA.getIntentos());
		entidad.setUsado(entidadJPA.isUsado());
		entidad.setCreadoEn(entidadJPA.getCreadoEn());
		return entidad;
	}

	@Override
	public RecuperacionClaveEntidadJpa ConvertirAEntidadJPA(RecuperacionClaveEntidad entidad) {
		var entidadJPA = new RecuperacionClaveEntidadJpa();
		if (entidad == null) {
			return entidadJPA;
		}
		entidadJPA.setId(entidad.getId());
		entidadJPA.setIdUsuario(entidad.getIdUsuario());
		entidadJPA.setCodigoHash(entidad.getCodigoHash());
		entidadJPA.setExpiraEn(entidad.getExpiraEn());
		entidadJPA.setIntentos(entidad.getIntentos());
		entidadJPA.setUsado(entidad.isUsado());
		entidadJPA.setCreadoEn(entidad.getCreadoEn());
		return entidadJPA;
	}

}
