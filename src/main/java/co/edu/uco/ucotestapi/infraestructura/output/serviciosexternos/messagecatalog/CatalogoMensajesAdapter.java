package co.edu.uco.ucotestapi.infraestructura.output.serviciosexternos.messagecatalog;

import java.util.Locale;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CatalogoMensajesServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.MensajeDTO;


@Component
public class CatalogoMensajesAdapter implements CatalogoMensajesServicio {
	private static final Locale IDIOMA = Locale.forLanguageTag("es-CO");

	private final MessageSource messageSource;

	public CatalogoMensajesAdapter(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public MensajeDTO consultarMensaje(CodigoMensaje codigo, Object... parametros) {
		Objects.requireNonNull(codigo, "El codigo del mensaje es obligatorio");
		var contenido = messageSource.getMessage(codigo.name(), convertirATexto(parametros), IDIOMA);
		return new MensajeDTO(codigo, contenido);
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