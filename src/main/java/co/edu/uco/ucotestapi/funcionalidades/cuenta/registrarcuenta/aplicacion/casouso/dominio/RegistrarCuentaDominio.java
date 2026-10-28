package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio;

import java.util.Locale;

import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador.ValidarClaveCuenta;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador.ValidarCorreoCuenta;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador.ValidarNombreCuenta;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio.validador.ValidarRolCuenta;
import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Cuenta a registrar. Valida cada dato al construirse, en el mismo orden de las reglas: rol, nombre, correo, clave.
 */
public class RegistrarCuentaDominio {
	private RolUsuario rol;
	private String nombre;
	private String correo;
	private String clave;

	public RegistrarCuentaDominio(final String nombre, final String correo, final String clave, final String rol) {
		setRol(rol);
		setNombre(nombre);
		setCorreo(correo);
		setClave(clave);
	}

	public RolUsuario getRol() {
		return rol;
	}

	public String getNombre() {
		return nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public String getClave() {
		return clave;
	}

	private void setRol(final String rol) {
		this.rol = ValidarRolCuenta.ejecutarValidacion(UtilTexto.aplicarTrim(rol).toUpperCase(Locale.ROOT));
	}

	private void setNombre(final String nombre) {
		this.nombre = UtilTexto.aplicarTrim(nombre);
		ValidarNombreCuenta.ejecutarValidacion(this.nombre);
	}

	private void setCorreo(final String correo) {
		this.correo = UtilTexto.aplicarTrim(correo).toLowerCase(Locale.ROOT);
		ValidarCorreoCuenta.ejecutarValidacion(this.correo);
	}

	private void setClave(final String clave) {
		this.clave = UtilTexto.obtenerValorDefecto(clave);
		ValidarClaveCuenta.ejecutarValidacion(this.clave);
	}

}