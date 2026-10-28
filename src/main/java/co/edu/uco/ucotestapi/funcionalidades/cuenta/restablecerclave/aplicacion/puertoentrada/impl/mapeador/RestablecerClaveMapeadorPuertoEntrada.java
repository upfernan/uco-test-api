package co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.puertoentrada.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.casouso.dominio.RestablecerClaveDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.puertoentrada.dto.RestablecerClaveDTO;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;

@Component
public class RestablecerClaveMapeadorPuertoEntrada implements MapeadorDTO<RestablecerClaveDTO, RestablecerClaveDominio> {
	private static final RestablecerClaveDTO DATOS_VACIOS = new RestablecerClaveDTO("", "", "");

	@Override
	public RestablecerClaveDominio aDominio(final RestablecerClaveDTO dto) {
		var datos = UtilObjeto.obtenerValorDefecto(dto, DATOS_VACIOS);
		return new RestablecerClaveDominio(datos.correo(), datos.codigo(), datos.claveNueva());
	}

}
