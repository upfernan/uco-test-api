package co.edu.uco.ucotestapi.servicios.casosuso.usuario;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante.DocenteDominio;
import co.edu.uco.ucotestapi.servicios.input.dto.request.IniciarSesionDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.response.SesionResponseDTO;
import co.edu.uco.ucotestapi.servicios.input.ports.usuario.IniciarSesionCasoUso;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.DocenteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.EstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.UsuarioRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.UsuarioEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.DatosSesionDTO;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.ProveedorIdentidadServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CatalogoMensajesServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

@Service
public class IniciarSesionCasoUsoImpl implements IniciarSesionCasoUso {
	private static final IniciarSesionDTO DATOS_VACIOS = new IniciarSesionDTO("", "");

	private final UsuarioRepositorio usuarioRepositorio;
	private final DocenteRepositorio docenteRepositorio;
	private final EstudianteRepositorio estudianteRepositorio;
	private final ProveedorIdentidadServicio proveedorIdentidad;
	private final CatalogoMensajesServicio catalogoMensajes;

	public IniciarSesionCasoUsoImpl(UsuarioRepositorio usuarioRepositorio, DocenteRepositorio docenteRepositorio,
			EstudianteRepositorio estudianteRepositorio, ProveedorIdentidadServicio proveedorIdentidad,
			CatalogoMensajesServicio catalogoMensajes) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.docenteRepositorio = docenteRepositorio;
		this.estudianteRepositorio = estudianteRepositorio;
		this.proveedorIdentidad = proveedorIdentidad;
		this.catalogoMensajes = catalogoMensajes;
	}

	@Override
	@Transactional(readOnly = true)
	public SesionResponseDTO ejecutar(IniciarSesionDTO datos) {
		try {
			var datosValidos = UtilObjeto.obtenerValorDefecto(datos, DATOS_VACIOS);
			var usuario = validarCredenciales(datosValidos);
			var nombre = validarPersonaYObtenerNombre(usuario);
			var token = proveedorIdentidad.emitirToken(new DatosSesionDTO(usuario.id(), usuario.correo(), nombre,
					usuario.rol(), usuario.idPersona()));
			return new SesionResponseDTO(token.token(), token.expiraEn(), usuario.rol().name(), nombre,
					usuario.idPersona());
		} catch (ReglaNegocioException excepcion) {
			throw UcoTestApiExcepcion.crear(excepcion,
					mensaje(excepcion.getCodigo(), excepcion.getParametros()),
					mensaje(CodigoMensaje.REGLA_NEGOCIO_INCUMPLIDA_TECNICO, excepcion.getCodigo().name()));
		} catch (UcoTestApiExcepcion excepcion) {
			throw excepcion;
		} catch (Exception excepcion) {
			throw UcoTestApiExcepcion.crear(excepcion,
					mensaje(CodigoMensaje.INICIAR_SESION_ERROR_INESPERADO_USUARIO),
					mensaje(CodigoMensaje.INICIAR_SESION_ERROR_INESPERADO_TECNICO, excepcion.getClass().getSimpleName()));
		}
	}

	// Regla de login 6 y 7: el correo debe existir y la clave coincidir, sin revelar cual de los dos fallo
	private UsuarioEntidad validarCredenciales(IniciarSesionDTO datos) {
		var correo = UtilTexto.aplicarTrim(datos.correo()).toLowerCase(Locale.ROOT);
		var clave = UtilTexto.obtenerValorDefecto(datos.clave());
		return usuarioRepositorio.consultarPorCorreo(correo)
				.filter(usuario -> proveedorIdentidad.claveCoincide(clave, usuario.claveHash()))
				.orElseThrow(() -> new ReglaNegocioException(CodigoMensaje.CREDENCIALES_INVALIDAS));
	}

	// Regla de login 6: si es docente, debe estar activo (regla 7 del registro de notas)
	private String validarPersonaYObtenerNombre(UsuarioEntidad usuario) {
		if (usuario.rol() == RolUsuario.DOCENTE) {
			new DocenteDominio(usuario.idDocente(), docenteRepositorio).validarDocenteActivo();
			return docenteRepositorio.consultarPorId(usuario.idDocente()).map(DocenteEntidad::getNombre)
					.orElseThrow(() -> new ReglaNegocioException(CodigoMensaje.CREDENCIALES_INVALIDAS));
		}
		return estudianteRepositorio.consultarPorId(usuario.idEstudiante()).map(EstudianteEntidad::getNombre)
				.orElseThrow(() -> new ReglaNegocioException(CodigoMensaje.CREDENCIALES_INVALIDAS));
	}

	private String mensaje(CodigoMensaje codigo, Object... parametros) {
		return catalogoMensajes.consultarMensaje(codigo, parametros).contenido();
	}

}
