package co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.casouso.impl;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.casouso.SolicitarRecuperacionClaveCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.casouso.dominio.SolicitarRecuperacionClaveDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertosalida.CodigoRecuperacionDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertosalida.CodigoRecuperacionPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.RecuperacionClaveRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.RecuperacionClaveEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.parametros.CatalogoParametros;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

/**
 * Si el correo tiene cuenta, crea un codigo de 6 digitos que vence en 15 minutos, guarda solo su version cifrada
 * y lo envia al usuario. Si no tiene cuenta no hace nada: asi nadie puede averiguar que correos estan registrados.
 */
@Service
public class SolicitarRecuperacionClaveCasoUsoImpl implements SolicitarRecuperacionClaveCasoUso {
	private static final SecureRandom ALEATORIO = new SecureRandom();

	private final UsuarioRepositorio usuarioRepositorio;
	private final RecuperacionClaveRepositorio recuperacionRepositorio;
	private final ProveedorIdentidadPuertoSalida proveedorIdentidad;
	private final CodigoRecuperacionPuertoSalida envioCodigo;

	public SolicitarRecuperacionClaveCasoUsoImpl(final UsuarioRepositorio usuarioRepositorio,
			final RecuperacionClaveRepositorio recuperacionRepositorio, final ProveedorIdentidadPuertoSalida proveedorIdentidad,
			final CodigoRecuperacionPuertoSalida envioCodigo) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.recuperacionRepositorio = recuperacionRepositorio;
		this.proveedorIdentidad = proveedorIdentidad;
		this.envioCodigo = envioCodigo;
	}

	@Override
	public Void ejecutar(final SolicitarRecuperacionClaveDominio datos) {
		usuarioRepositorio.consultarPorCorreo(datos.getCorreo()).ifPresent(this::crearYEnviarCodigo);
		return null;
	}

	private void crearYEnviarCodigo(final UsuarioEntidad usuario) {
		recuperacionRepositorio.anularPendientes(usuario.id());
		var codigo = generarCodigo();
		var ahora = Instant.now();
		var recuperacion = new RecuperacionClaveEntidad();
		recuperacion.setId(UtilUUID.generar());
		recuperacion.setIdUsuario(usuario.id());
		recuperacion.setCodigoHash(proveedorIdentidad.cifrarClave(codigo));
		recuperacion.setExpiraEn(ahora.plus(Duration.ofMinutes(CatalogoParametros.RECUPERACION_CLAVE_MINUTOS_VIGENCIA)));
		recuperacion.setCreadoEn(ahora);
		recuperacionRepositorio.crear(recuperacion);
		envioCodigo.enviar(new CodigoRecuperacionDTO(usuario.correo(),
				CatalogoMensajesProveedor.contenido(CodigoMensaje.RECUPERACION_CLAVE_ASUNTO),
				CatalogoMensajesProveedor.contenido(CodigoMensaje.RECUPERACION_CLAVE_CONTENIDO, codigo,
						CatalogoParametros.RECUPERACION_CLAVE_MINUTOS_VIGENCIA)));
	}

	private static String generarCodigo() {
		var limite = (int) Math.pow(10, CatalogoParametros.RECUPERACION_CLAVE_DIGITOS);
		return String.format("%0" + CatalogoParametros.RECUPERACION_CLAVE_DIGITOS + "d", ALEATORIO.nextInt(limite));
	}

}
