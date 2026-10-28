package co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorRespuestaDTO;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.casouso.dominio.EstudianteDelGrupoDominio;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.dto.EstudianteGrupoDTO;

@Component
public class ConsultarEstudiantesGrupoMapeadorPuertoEntrada implements MapeadorRespuestaDTO<EstudianteDelGrupoDominio, EstudianteGrupoDTO> {

	@Override
	public EstudianteGrupoDTO aDTO(final EstudianteDelGrupoDominio dominio) {
		return new EstudianteGrupoDTO(dominio.id(), dominio.nombre());
	}

}
