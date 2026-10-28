package co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorRespuestaDTO;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.dominio.GrupoDocenteDominio;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.dto.GrupoDocenteDTO;

@Component
public class ConsultarGruposDocenteMapeadorPuertoEntrada implements MapeadorRespuestaDTO<GrupoDocenteDominio, GrupoDocenteDTO> {

	@Override
	public GrupoDocenteDTO aDTO(final GrupoDocenteDominio dominio) {
		return new GrupoDocenteDTO(dominio.id(), dominio.nombre(), dominio.estado(), dominio.fechaInicio(), dominio.fechaFin());
	}

}
