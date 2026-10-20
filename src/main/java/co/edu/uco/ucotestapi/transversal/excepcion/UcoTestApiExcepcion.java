package co.edu.uco.ucotestapi.transversal.excepcion;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

public final class UcoTestApiExcepcion extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final Throwable excepcionRaiz;
	private final String mensajeUsuario;
	private final String mensajeTecnico;

	private UcoTestApiExcepcion(final Throwable excepcionRaiz, final String mensajeUsuario, final String mensajeTecnico) {
		super(UtilTexto.obtenerValorDefecto(mensajeTecnico), excepcionRaiz);
		this.excepcionRaiz = UtilObjeto.obtenerValorDefecto(excepcionRaiz, new Exception());
		this.mensajeUsuario = UtilTexto.obtenerValorDefecto(mensajeUsuario);
		this.mensajeTecnico = UtilTexto.obtenerValorDefecto(mensajeTecnico);
	}

	public static UcoTestApiExcepcion crear(final String mensajeUsuario) {
		return new UcoTestApiExcepcion(new Exception(), mensajeUsuario, mensajeUsuario);
	}

	public static UcoTestApiExcepcion crear(final String mensajeUsuario, final String mensajeTecnico) {
		return new UcoTestApiExcepcion(new Exception(), mensajeUsuario, mensajeTecnico);
	}

	public static UcoTestApiExcepcion crear(final Throwable excepcionRaiz, final String mensajeUsuario) {
		return new UcoTestApiExcepcion(excepcionRaiz, mensajeUsuario, mensajeUsuario);
	}

	public static UcoTestApiExcepcion crear(final Throwable excepcionRaiz, final String mensajeUsuario, final String mensajeTecnico) {
		return new UcoTestApiExcepcion(excepcionRaiz, mensajeUsuario, mensajeTecnico);
	}

	public Throwable getExcepcionRaiz() {
		return excepcionRaiz;
	}

	public String getMensajeUsuario() {
		return mensajeUsuario;
	}

	public String getMensajeTecnico() {
		return mensajeTecnico;
	}

}