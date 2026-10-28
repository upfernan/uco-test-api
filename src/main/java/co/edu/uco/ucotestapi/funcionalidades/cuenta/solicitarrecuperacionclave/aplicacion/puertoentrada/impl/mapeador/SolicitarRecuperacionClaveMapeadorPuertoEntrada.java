package co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertoentrada.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.casouso.dominio.SolicitarRecuperacionClaveDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertoentrada.dto.SolicitarRecuperacionClaveDTO;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;

@Component
public class SolicitarRecuperacionClaveMapeadorPuertoEntrada
		implements MapeadorDTO<SolicitarRecuperacionClaveDTO, SolicitarRecuperacionClaveDominio> {
	private static final SolicitarRecuperacionClaveDTO DATOS_VACIOS = new SolicitarRecuperacionClaveDTO("");

	@Override
	public SolicitarRecuperacionClaveDominio aDominio(final SolicitarRecuperacionClaveDTO dto) {
		return new SolicitarRecuperacionClaveDominio(UtilObjeto.obtenerValorDefecto(dto, DATOS_VACIOS).correo());
	}

}
