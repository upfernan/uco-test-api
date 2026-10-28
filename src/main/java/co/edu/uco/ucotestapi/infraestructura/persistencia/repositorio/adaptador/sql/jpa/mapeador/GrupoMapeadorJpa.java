package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.GrupoEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.DocenteEntidadJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;


@Component
public class GrupoMapeadorJpa implements MapeadorJpa<GrupoEntidad, GrupoEntidadJpa> {
	private final MapeadorJpa<DocenteEntidad, DocenteEntidadJpa> docenteMapper;

	public GrupoMapeadorJpa(MapeadorJpa<DocenteEntidad, DocenteEntidadJpa> docenteMapper) {
		this.docenteMapper = docenteMapper;
	}

	@Override
	public GrupoEntidad ConvertirAEntidad(GrupoEntidadJpa entidadJPA) {
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
	public GrupoEntidadJpa ConvertirAEntidadJPA(GrupoEntidad entidad) {
		if (entidad == null) {
			throw new IllegalArgumentException("La entidad de grupo no puede ser nula");
		}
		var entidadJPA = new GrupoEntidadJpa();
		entidadJPA.setId(entidad.getId());
		entidadJPA.setNombre(entidad.getNombre());
		entidadJPA.setEstado(entidad.getEstado());
		entidadJPA.setFechaInicio(entidad.getFechaInicio());
		entidadJPA.setFechaFin(entidad.getFechaFin());
		entidadJPA.setDocente(docenteMapper.ConvertirAEntidadJPA(entidad.getDocente()));
		return entidadJPA;
	}

}