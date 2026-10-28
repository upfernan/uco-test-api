package co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.impl.mapeador;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorDTO;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.dominio.RegistrarNotaEstudianteDominio;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.dto.RegistrarNotaEstudianteDTO;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Component
public class RegistrarNotaEstudianteMapeadorPuertoEntrada implements MapeadorDTO<RegistrarNotaEstudianteDTO, RegistrarNotaEstudianteDominio> {
	private static final RegistrarNotaEstudianteDTO DATOS_VACIOS = new RegistrarNotaEstudianteDTO(UtilUUID.UUID_DEFECTO,
			UtilUUID.UUID_DEFECTO, UtilUUID.UUID_DEFECTO, UtilUUID.UUID_DEFECTO, BigDecimal.ZERO);

	@Override
	public RegistrarNotaEstudianteDominio aDominio(final RegistrarNotaEstudianteDTO dto) {
		var datos = UtilObjeto.obtenerValorDefecto(dto, DATOS_VACIOS);
		return new RegistrarNotaEstudianteDominio(datos.idDocente(), datos.idGrupo(), datos.idEstudiante(), datos.idNota(),
				datos.valor());
	}

}