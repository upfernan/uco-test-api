package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.catalogomensajes;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.propiedades.adaptador.CatalogoMensajesPropiedadesAdaptador;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio.adaptador.CatalogoMensajesServicioAdaptador;

class CatalogoMensajesConfiguracionTest {
	private final CatalogoMensajesConfiguracion configuracion = new CatalogoMensajesConfiguracion();
	private final ResourceBundleMessageSource mensajes = new ResourceBundleMessageSource();

	@Test
	void conUrlUsaElServicioExterno() {
		var url = "https://catalogo.prueba";
		assertInstanceOf(CatalogoMensajesServicioAdaptador.class,
				configuracion.catalogoMensajesPuertoSalida(mensajes, configuracion.clientePlantillasMensajes(url), url));
	}

	@Test
	void sinUrlUsaElArchivo() {
		assertInstanceOf(CatalogoMensajesPropiedadesAdaptador.class,
				configuracion.catalogoMensajesPuertoSalida(mensajes, configuracion.clientePlantillasMensajes(""), " "));
	}

}
