package co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorDTO;
import co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador.MapeadorRespuestaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.dominio.IniciarSesionDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.dominio.SesionIniciadaDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.dto.IniciarSesionDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.dto.SesionIniciadaDTO;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;

@Component
public class IniciarSesionMapeadorPuertoEntrada implements MapeadorDTO<IniciarSesionDTO, IniciarSesionDominio>,
		MapeadorRespuestaDTO<SesionIniciadaDominio, SesionIniciadaDTO> {
	private static final IniciarSesionDTO DATOS_VACIOS = new IniciarSesionDTO("", "");

	@Override
	public IniciarSesionDominio aDominio(final IniciarSesionDTO dto) {
		var datos = UtilObjeto.obtenerValorDefecto(dto, DATOS_VACIOS);
		return new IniciarSesionDominio(datos.correo(), datos.clave());
	}

	@Override
	public SesionIniciadaDTO aDTO(final SesionIniciadaDominio dominio) {
		return new SesionIniciadaDTO(dominio.token(), dominio.expiraEn(), dominio.rol().name(), dominio.nombre(), dominio.idPersona());
	}

}