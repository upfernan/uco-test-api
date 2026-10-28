package co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucotestapi.aplicacion.casouso.validador.generales.docente.ValidarDocenteActivo;
import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.DatosSesionDTO;
import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.IniciarSesionCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.dominio.IniciarSesionDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.dominio.SesionIniciadaDominio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;

@Service
public class IniciarSesionCasoUsoImpl implements IniciarSesionCasoUso {
	private final UsuarioRepositorio usuarioRepositorio;
	private final DocenteRepositorio docenteRepositorio;
	private final EstudianteRepositorio estudianteRepositorio;
	private final ProveedorIdentidadPuertoSalida proveedorIdentidad;

	public IniciarSesionCasoUsoImpl(final UsuarioRepositorio usuarioRepositorio, final DocenteRepositorio docenteRepositorio,
			final EstudianteRepositorio estudianteRepositorio, final ProveedorIdentidadPuertoSalida proveedorIdentidad) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.docenteRepositorio = docenteRepositorio;
		this.estudianteRepositorio = estudianteRepositorio;
		this.proveedorIdentidad = proveedorIdentidad;
	}

	@Override
	public SesionIniciadaDominio ejecutar(final IniciarSesionDominio credenciales) {
		var usuario = validarCredenciales(credenciales);
		var nombre = validarPersonaYObtenerNombre(usuario);
		var token = proveedorIdentidad.emitirToken(new DatosSesionDTO(usuario.id(), usuario.correo(), nombre, usuario.rol(),
				usuario.idPersona()));
		return new SesionIniciadaDominio(token.token(), token.expiraEn(), usuario.rol(), nombre, usuario.idPersona());
	}

	// Reglas de login 6 y 7: el correo debe existir y la clave coincidir, sin revelar cual de los dos fallo
	private UsuarioEntidad validarCredenciales(final IniciarSesionDominio credenciales) {
		return usuarioRepositorio.consultarPorCorreo(credenciales.getCorreo())
				.filter(usuario -> proveedorIdentidad.claveCoincide(credenciales.getClave(), usuario.claveHash()))
				.orElseThrow(() -> CatalogoMensajesProveedor.excepcion(CodigoMensaje.CREDENCIALES_INVALIDAS));
	}

	// Regla de login 6: si es docente, debe estar activo
	private String validarPersonaYObtenerNombre(final UsuarioEntidad usuario) {
		if (usuario.rol() == RolUsuario.DOCENTE) {
			var docente = docenteRepositorio.consultarPorId(usuario.idDocente())
					.orElseThrow(() -> CatalogoMensajesProveedor.excepcion(CodigoMensaje.CREDENCIALES_INVALIDAS));
			ValidarDocenteActivo.ejecutarValidacion(docente);
			return docente.getNombre();
		}
		return estudianteRepositorio.consultarPorId(usuario.idEstudiante()).map(EstudianteEntidad::getNombre)
				.orElseThrow(() -> CatalogoMensajesProveedor.excepcion(CodigoMensaje.CREDENCIALES_INVALIDAS));
	}

}