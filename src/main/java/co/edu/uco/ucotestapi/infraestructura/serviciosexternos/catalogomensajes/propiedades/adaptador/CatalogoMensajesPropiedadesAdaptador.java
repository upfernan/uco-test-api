package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.propiedades.adaptador;

import java.util.Locale;
import java.util.Objects;

import org.springframework.context.MessageSource;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.CatalogoMensajesPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.MensajeCatalogoDTO;


/**
 * Lee los mensajes del archivo mensajes.properties de la API. Se usa en local, en las pruebas
 * y como respaldo cuando el servicio externo del catalogo no responde.
 */
public class CatalogoMensajesPropiedadesAdaptador implements CatalogoMensajesPuertoSalida {
	private static final Locale IDIOMA = Locale.forLanguageTag("es-CO");

	private final MessageSource messageSource;

	public CatalogoMensajesPropiedadesAdaptador(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public MensajeCatalogoDTO consultarMensaje(CodigoMensaje codigo, Object... parametros) {
		Objects.requireNonNull(codigo, "El codigo del mensaje es obligatorio");
		var contenido = messageSource.getMessage(codigo.name(), convertirATexto(parametros), IDIOMA);
		return new MensajeCatalogoDTO(codigo, contenido);
	}

	private Object[] convertirATexto(Object... parametros) {
		if (parametros == null) {
			return new Object[0];
		}
		var textos = new Object[parametros.length];
		for (int i = 0; i < parametros.length; i++) {
			textos[i] = String.valueOf(parametros[i]);
		}
		return textos;
	}

}