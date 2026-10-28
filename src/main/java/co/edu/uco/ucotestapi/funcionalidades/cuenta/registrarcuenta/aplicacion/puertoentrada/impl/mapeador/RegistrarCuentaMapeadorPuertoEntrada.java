package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorDTO;
import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorRespuestaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.CuentaRegistradaDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.RegistrarCuentaDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.dto.CuentaRegistradaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.dto.RegistrarCuentaDTO;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;

@Component
public class RegistrarCuentaMapeadorPuertoEntrada implements MapeadorDTO<RegistrarCuentaDTO, RegistrarCuentaDominio>,
		MapeadorRespuestaDTO<CuentaRegistradaDominio, CuentaRegistradaDTO> {
	private static final RegistrarCuentaDTO DATOS_VACIOS = new RegistrarCuentaDTO("", "", "", "");

	@Override
	public RegistrarCuentaDominio aDominio(final RegistrarCuentaDTO dto) {
		var datos = UtilObjeto.obtenerValorDefecto(dto, DATOS_VACIOS);
		return new RegistrarCuentaDominio(datos.nombre(), datos.correo(), datos.clave(), datos.rol());
	}

	@Override
	public CuentaRegistradaDTO aDTO(final CuentaRegistradaDominio dominio) {
		return new CuentaRegistradaDTO(dominio.id(), dominio.correo(), dominio.rol().name(), dominio.idPersona());
	}

}