package co.edu.uco.ucotestapi.soporte;

import org.springframework.context.support.ResourceBundleMessageSource;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.propiedades.adaptador.CatalogoMensajesPropiedadesAdaptador;

/**
 * Inicializa el catalogo de mensajes real (mensajes.properties) para pruebas unitarias sin Spring.
 */
public final class CatalogoMensajesPrueba {

	private CatalogoMensajesPrueba() {
		super();
	}

	public static void inicializar() {
		var mensajes = new ResourceBundleMessageSource();
		mensajes.setBasename("mensajes");
		mensajes.setDefaultEncoding("UTF-8");
		CatalogoMensajesProveedor.inicializar(new CatalogoMensajesPropiedadesAdaptador(mensajes));
	}

}