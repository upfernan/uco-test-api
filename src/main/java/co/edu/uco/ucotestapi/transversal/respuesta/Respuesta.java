package co.edu.uco.ucotestapi.transversal.respuesta;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

/**
 * Formato unico de todas las respuestas de la API: mensajes, datos y si fue exitosa.
 */
public class Respuesta<T> {
	private List<String> mensajes;
	private List<T> datos;
	private boolean respuestaExitosa;

	public Respuesta(final List<String> mensajes, final List<T> datos, final boolean respuestaExitosa) {
		setMensajes(mensajes);
		setDatos(datos);
		setRespuestaExitosa(respuestaExitosa);
	}

	public static <T> Respuesta<T> crearExitosa() {
		return new Respuesta<>(new ArrayList<>(), new ArrayList<>(), true);
	}

	public static <T> Respuesta<T> crearExitosa(final List<T> datos) {
		return new Respuesta<>(new ArrayList<>(), datos, true);
	}

	public static <T> Respuesta<T> crearExitosa(final T dato) {
		var datos = new ArrayList<T>();
		datos.add(dato);
		return crearExitosa(datos);
	}

	public static <T> Respuesta<T> crearFallida() {
		return new Respuesta<>(new ArrayList<>(), new ArrayList<>(), false);
	}

	public static <T> Respuesta<T> crearFallida(final String mensaje) {
		Respuesta<T> respuesta = crearFallida();
		respuesta.agregarMensaje(mensaje);
		return respuesta;
	}

	public Respuesta<T> agregarMensaje(final String mensaje) {
		if (UtilTexto.tieneContenido(mensaje)) {
			mensajes.add(UtilTexto.aplicarTrim(mensaje));
		}
		return this;
	}

	public List<String> getMensajes() {
		return mensajes;
	}

	public void setMensajes(final List<String> mensajes) {
		this.mensajes = new ArrayList<>(UtilObjeto.obtenerValorDefecto(mensajes, new ArrayList<>()));
	}

	public List<T> getDatos() {
		return datos;
	}

	public void setDatos(final List<T> datos) {
		this.datos = new ArrayList<>(UtilObjeto.obtenerValorDefecto(datos, new ArrayList<>()));
	}

	public boolean isRespuestaExitosa() {
		return respuestaExitosa;
	}

	public void setRespuestaExitosa(final boolean respuestaExitosa) {
		this.respuestaExitosa = respuestaExitosa;
	}

}
