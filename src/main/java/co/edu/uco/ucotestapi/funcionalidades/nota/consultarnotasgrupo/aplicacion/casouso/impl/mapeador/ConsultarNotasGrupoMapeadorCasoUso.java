package co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.casouso.mapeador.MapeadorDominio;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.dominio.NotaDelGrupoDominio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;

@Component
public class ConsultarNotasGrupoMapeadorCasoUso implements MapeadorDominio<NotaDelGrupoDominio, NotaEntidad> {

	@Override
	public NotaDelGrupoDominio aDominio(final NotaEntidad entidad) {
		return new NotaDelGrupoDominio(entidad.getId(), entidad.getNombre(), entidad.getEstado().name(), entidad.getFechaRegistroDesde(),
				entidad.getFechaRegistroHasta());
	}

}
