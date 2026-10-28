package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.RegistrarCuentaCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.CuentaRegistradaDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.RegistrarCuentaDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador.ValidarCorreoNoRegistrado;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoDocente;
import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class RegistrarCuentaCasoUsoImpl implements RegistrarCuentaCasoUso {
	private final UsuarioRepositorio usuarioRepositorio;
	private final DocenteRepositorio docenteRepositorio;
	private final EstudianteRepositorio estudianteRepositorio;
	private final ProveedorIdentidadPuertoSalida proveedorIdentidad;

	public RegistrarCuentaCasoUsoImpl(final UsuarioRepositorio usuarioRepositorio, final DocenteRepositorio docenteRepositorio,
			final EstudianteRepositorio estudianteRepositorio, final ProveedorIdentidadPuertoSalida proveedorIdentidad) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.docenteRepositorio = docenteRepositorio;
		this.estudianteRepositorio = estudianteRepositorio;
		this.proveedorIdentidad = proveedorIdentidad;
	}

	@Override
	public CuentaRegistradaDominio ejecutar(final RegistrarCuentaDominio cuenta) {
		ValidarCorreoNoRegistrado.ejecutarValidacion(cuenta.getCorreo(), usuarioRepositorio);

		var id = UtilUUID.generar();
		var claveCifrada = proveedorIdentidad.cifrarClave(cuenta.getClave());
		var usuario = cuenta.getRol() == RolUsuario.DOCENTE
				? UsuarioEntidad.deDocente(id, cuenta.getCorreo(), claveCifrada, obtenerOCrearDocente(cuenta))
				: UsuarioEntidad.deEstudiante(id, cuenta.getCorreo(), claveCifrada, obtenerOCrearEstudiante(cuenta));
		usuarioRepositorio.crear(usuario);
		return new CuentaRegistradaDominio(usuario.id(), usuario.correo(), usuario.rol(), usuario.idPersona());
	}

	// Regla de registro 5: si ya existe un docente con ese correo y sin cuenta, la cuenta se asocia a el
	private UUID obtenerOCrearDocente(final RegistrarCuentaDominio cuenta) {
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
	private UUID obtenerOCrearEstudiante(final RegistrarCuentaDominio cuenta) {
		return estudianteRepositorio.consultarPorCorreo(cuenta.getCorreo()).map(EstudianteEntidad::getId).orElseGet(() -> {
			var estudiante = new EstudianteEntidad();
			estudiante.setId(UtilUUID.generar());
			estudiante.setNombre(cuenta.getNombre());
			estudiante.setCorreo(cuenta.getCorreo());
			estudianteRepositorio.crear(estudiante);
			return estudiante.getId();
		});
	}

}