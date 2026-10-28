package co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.casouso.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.casouso.RestablecerClaveCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.casouso.dominio.RestablecerClaveDominio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.RecuperacionClaveRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.RecuperacionClaveEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;
import co.edu.uco.ucotestapi.transversal.parametros.CatalogoParametros;

/**
 * Cambia la contrasena si el codigo es el ultimo pedido, no ha vencido, no se ha usado y no supero los 5 intentos.
 * Cualquier falla responde el mismo mensaje, sin decir cual de las condiciones no se cumplio.
 */
@Service
public class RestablecerClaveCasoUsoImpl implements RestablecerClaveCasoUso {
	private final UsuarioRepositorio usuarioRepositorio;
	private final RecuperacionClaveRepositorio recuperacionRepositorio;
	private final ProveedorIdentidadPuertoSalida proveedorIdentidad;

	public RestablecerClaveCasoUsoImpl(final UsuarioRepositorio usuarioRepositorio,
			final RecuperacionClaveRepositorio recuperacionRepositorio, final ProveedorIdentidadPuertoSalida proveedorIdentidad) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.recuperacionRepositorio = recuperacionRepositorio;
		this.proveedorIdentidad = proveedorIdentidad;
	}

	@Override
	public Void ejecutar(final RestablecerClaveDominio datos) {
		datos.validarClaveNueva();
		var usuario = usuarioRepositorio.consultarPorCorreo(datos.getCorreo()).orElseThrow(RestablecerClaveCasoUsoImpl::codigoInvalido);
		var recuperacion = recuperacionRepositorio.consultarPendientePorUsuario(usuario.id())
				.orElseThrow(RestablecerClaveCasoUsoImpl::codigoInvalido);
		validarVigente(recuperacion);
		validarCodigo(recuperacion, datos.getCodigo());

		recuperacion.setUsado(true);
		recuperacionRepositorio.modificar(recuperacion.getId(), recuperacion);
		usuarioRepositorio.modificar(usuario.id(), new UsuarioEntidad(usuario.id(), usuario.correo(),
				proveedorIdentidad.cifrarClave(datos.getClaveNueva()), usuario.rol(), usuario.idDocente(), usuario.idEstudiante()));
		return null;
	}

	private static void validarVigente(final RecuperacionClaveEntidad recuperacion) {
		if (Instant.now().isAfter(recuperacion.getExpiraEn())
				|| recuperacion.getIntentos() >= CatalogoParametros.RECUPERACION_CLAVE_MAXIMO_INTENTOS) {
			throw codigoInvalido();
		}
	}

	private void validarCodigo(final RecuperacionClaveEntidad recuperacion, final String codigo) {
		if (!proveedorIdentidad.claveCoincide(codigo, recuperacion.getCodigoHash())) {
			recuperacion.setIntentos(recuperacion.getIntentos() + 1);
			recuperacionRepositorio.modificar(recuperacion.getId(), recuperacion);
			throw codigoInvalido();
		}
	}

	private static UcoTestApiExcepcion codigoInvalido() {
		return CatalogoMensajesProveedor.excepcion(CodigoMensaje.CODIGO_RECUPERACION_INVALIDO);
	}

}
