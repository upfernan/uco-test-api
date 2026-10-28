package co.edu.uco.ucotestapi.dominio.usuario.registrarcuenta;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.UsuarioRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

public class CuentaDominio {
	private static final int NOMBRE_MINIMO = 2;
	private static final int NOMBRE_MAXIMO = 100;
	private static final int CLAVE_MINIMA = 8;
	private static final Pattern FORMATO_CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	private static final Pattern TIENE_LETRA = Pattern.compile("\\p{L}");
	private static final Pattern TIENE_NUMERO = Pattern.compile("\\d");

	private final String nombre;
	private final String correo;
	private final String clave;
	private final String rol;
	private final UsuarioRepositorio usuarioRepositorio;

	public CuentaDominio(String nombre, String correo, String clave, String rol, UsuarioRepositorio usuarioRepositorio) {
		this.nombre = UtilTexto.aplicarTrim(nombre);
		this.correo = UtilTexto.aplicarTrim(correo).toLowerCase(Locale.ROOT);
		this.clave = UtilTexto.obtenerValorDefecto(clave);
		this.rol = UtilTexto.aplicarTrim(rol).toUpperCase(Locale.ROOT);
		this.usuarioRepositorio = Objects.requireNonNull(usuarioRepositorio, "El repositorio de usuarios es obligatorio");
	}

	// Regla de registro 1
	public void validarNombre() {
		if (!UtilTexto.tieneLongitudValida(nombre, NOMBRE_MINIMO, NOMBRE_MAXIMO) || !UtilTexto.soloLetrasYEspacios(nombre)) {
			throw new ReglaNegocioException(CodigoMensaje.CUENTA_NOMBRE_INVALIDO);
		}
	}

	// Regla de registro 2: formato
	public void validarFormatoCorreo() {
		if (!FORMATO_CORREO.matcher(correo).matches()) {
			throw new ReglaNegocioException(CodigoMensaje.CUENTA_CORREO_INVALIDO);
		}
	}

	// Regla de registro 2: unico
	public void validarCorreoNoRegistrado() {
		if (usuarioRepositorio.existePorCorreo(correo)) {
			throw new ReglaNegocioException(CodigoMensaje.CUENTA_CORREO_YA_REGISTRADO);
		}
	}

	// Regla de registro 3
	public void validarClaveSegura() {
		if (clave.length() < CLAVE_MINIMA || !TIENE_LETRA.matcher(clave).find() || !TIENE_NUMERO.matcher(clave).find()) {
			throw new ReglaNegocioException(CodigoMensaje.CUENTA_CLAVE_INSEGURA);
		}
	}

	// Regla de registro 4
	public void validarRol() {
		obtenerRol();
	}

	public RolUsuario obtenerRol() {
		for (var rolValido : RolUsuario.values()) {
			if (rolValido.name().equals(rol)) {
				return rolValido;
			}
		}
		throw new ReglaNegocioException(CodigoMensaje.CUENTA_ROL_INVALIDO);
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

}
