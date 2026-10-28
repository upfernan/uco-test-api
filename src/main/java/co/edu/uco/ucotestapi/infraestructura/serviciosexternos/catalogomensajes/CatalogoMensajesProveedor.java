package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.CatalogoMensajesPuertoSalida;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

/**
 * Acceso estatico al catalogo de mensajes para las reglas y validadores, que son singletons sin inyeccion.
 * Spring lo inicializa al arrancar; las pruebas unitarias lo inicializan con su propio adaptador.
 */
@Component
public final class CatalogoMensajesProveedor {
	private static CatalogoMensajesPuertoSalida catalogo;

	public CatalogoMensajesProveedor(final CatalogoMensajesPuertoSalida catalogoMensajes) {
		inicializar(catalogoMensajes);
	}

	public static void inicializar(final CatalogoMensajesPuertoSalida catalogoMensajes) {
		catalogo = catalogoMensajes;
	}

	public static String contenido(final CodigoMensaje codigo, final Object... parametros) {
		return catalogo.consultarMensaje(codigo, parametros).contenido();
	}

	public static UcoTestApiExcepcion excepcion(final CodigoMensaje codigo, final Object... parametros) {
		return UcoTestApiExcepcion.crear(contenido(codigo, parametros),
				contenido(CodigoMensaje.REGLA_NEGOCIO_INCUMPLIDA_TECNICO, codigo.name() + " " + Arrays.toString(parametros)));
	}

}