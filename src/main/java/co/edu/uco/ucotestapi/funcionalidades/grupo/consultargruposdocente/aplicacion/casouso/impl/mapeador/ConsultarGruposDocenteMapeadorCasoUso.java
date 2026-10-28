package co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.casouso.mapeador.MapeadorDominio;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.dominio.GrupoDocenteDominio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;

@Component
public class ConsultarGruposDocenteMapeadorCasoUso implements MapeadorDominio<GrupoDocenteDominio, GrupoEntidad> {

	@Override
	public GrupoDocenteDominio aDominio(final GrupoEntidad entidad) {
		return new GrupoDocenteDominio(entidad.getId(), entidad.getNombre(), entidad.getEstado().name(), entidad.getFechaInicio(),
				entidad.getFechaFin());
	}

}
