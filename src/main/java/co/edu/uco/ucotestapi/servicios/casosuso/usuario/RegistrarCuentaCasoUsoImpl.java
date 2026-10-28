package co.edu.uco.ucotestapi.servicios.casosuso.usuario;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.dominio.enums.EstadoDocente;
import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.dominio.usuario.registrarcuenta.CuentaDominio;
import co.edu.uco.ucotestapi.servicios.input.dto.request.RegistrarCuentaDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.response.CuentaResponseDTO;
import co.edu.uco.ucotestapi.servicios.input.ports.usuario.RegistrarCuentaCasoUso;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.DocenteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.EstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.UsuarioRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.UsuarioEntidad;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided.ProveedorIdentidadServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CatalogoMensajesServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class RegistrarCuentaCasoUsoImpl implements RegistrarCuentaCasoUso {
	private static final RegistrarCuentaDTO DATOS_VACIOS = new RegistrarCuentaDTO("", "", "", "");

	private final UsuarioRepositorio usuarioRepositorio;
	private final DocenteRepositorio docenteRepositorio;
	private final EstudianteRepositorio estudianteRepositorio;
	private final ProveedorIdentidadServicio proveedorIdentidad;
	private final CatalogoMensajesServicio catalogoMensajes;

	public RegistrarCuentaCasoUsoImpl(UsuarioRepositorio usuarioRepositorio, DocenteRepositorio docenteRepositorio,
			EstudianteRepositorio estudianteRepositorio, ProveedorIdentidadServicio proveedorIdentidad,
			CatalogoMensajesServicio catalogoMensajes) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.docenteRepositorio = docenteRepositorio;
		this.estudianteRepositorio = estudianteRepositorio;
		this.proveedorIdentidad = proveedorIdentidad;
		this.catalogoMensajes = catalogoMensajes;
	}

	@Override
	@Transactional
	public CuentaResponseDTO ejecutar(RegistrarCuentaDTO datos) {
		try {
			var datosValidos = UtilObjeto.obtenerValorDefecto(datos, DATOS_VACIOS);
			var cuenta = new CuentaDominio(datosValidos.nombre(), datosValidos.correo(), datosValidos.clave(),
					datosValidos.rol(), usuarioRepositorio);
			cuenta.validarRol();
			cuenta.validarNombre();
			cuenta.validarFormatoCorreo();
			cuenta.validarClaveSegura();
			cuenta.validarCorreoNoRegistrado();
			return registrar(cuenta);
		} catch (ReglaNegocioException excepcion) {
			throw UcoTestApiExcepcion.crear(excepcion,
					mensaje(excepcion.getCodigo(), excepcion.getParametros()),
					mensaje(CodigoMensaje.REGLA_NEGOCIO_INCUMPLIDA_TECNICO, excepcion.getMessage()));
		} catch (UcoTestApiExcepcion excepcion) {
			throw excepcion;
		} catch (Exception excepcion) {
			throw UcoTestApiExcepcion.crear(excepcion,
					mensaje(CodigoMensaje.REGISTRAR_CUENTA_ERROR_INESPERADO_USUARIO),
					mensaje(CodigoMensaje.REGISTRAR_CUENTA_ERROR_INESPERADO_TECNICO, excepcion.getClass().getSimpleName()));
		}
	}

	private CuentaResponseDTO registrar(CuentaDominio cuenta) {
		var rol = cuenta.obtenerRol();
		var id = UtilUUID.generar();
		var claveCifrada = proveedorIdentidad.cifrarClave(cuenta.getClave());
		var usuario = rol == RolUsuario.DOCENTE
				? UsuarioEntidad.deDocente(id, cuenta.getCorreo(), claveCifrada, obtenerOCrearDocente(cuenta))
				: UsuarioEntidad.deEstudiante(id, cuenta.getCorreo(), claveCifrada, obtenerOCrearEstudiante(cuenta));
		usuarioRepositorio.crear(usuario);
		return new CuentaResponseDTO(usuario.id(), usuario.correo(), rol.name(), usuario.idPersona());
	}

	// Regla de registro 5: si ya existe un docente con ese correo y sin cuenta, la cuenta se asocia a el
	private UUID obtenerOCrearDocente(CuentaDominio cuenta) {
		return docenteRepositorio.consultarPorCorreo(cuenta.getCorreo()).map(DocenteEntidad::getId).orElseGet(() -> {
			var docente = new DocenteEntidad();
			docente.setId(UtilUUID.generar());
			docente.setNombre(cuenta.getNombre());
			docente.setCorreo(cuenta.getCorreo());
			docente.setEstado(EstadoDocente.ACTIVO);
			docenteRepositorio.crear(docente);
			return docente.getId();
		});
	}

	// Regla de registro 5: si ya existe un estudiante con ese correo y sin cuenta, la cuenta se asocia a el
	private UUID obtenerOCrearEstudiante(CuentaDominio cuenta) {
		return estudianteRepositorio.consultarPorCorreo(cuenta.getCorreo()).map(EstudianteEntidad::getId).orElseGet(() -> {
			var estudiante = new EstudianteEntidad();
			estudiante.setId(UtilUUID.generar());
			estudiante.setNombre(cuenta.getNombre());
			estudiante.setCorreo(cuenta.getCorreo());
			estudianteRepositorio.crear(estudiante);
			return estudiante.getId();
		});
	}

	private String mensaje(CodigoMensaje codigo, Object... parametros) {
		return catalogoMensajes.consultarMensaje(codigo, parametros).contenido();
	}

}
