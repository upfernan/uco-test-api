package co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorRespuestaDTO;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.dominio.NotaDelGrupoDominio;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.dto.NotaGrupoDTO;

@Component
public class ConsultarNotasGrupoMapeadorPuertoEntrada implements MapeadorRespuestaDTO<NotaDelGrupoDominio, NotaGrupoDTO> {

	@Override
	public NotaGrupoDTO aDTO(final NotaDelGrupoDominio dominio) {
		return new NotaGrupoDTO(dominio.id(), dominio.nombre(), dominio.estado(), dominio.fechaRegistroDesde(), dominio.fechaRegistroHasta());
	}

}
