package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio.adaptador;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.CatalogoMensajesPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.MensajeCatalogoDTO;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio.ClientePlantillasMensajes;

/**
 * Toma los mensajes del servicio externo del catalogo (con cache en Redis) y reemplaza los parametros.
 * Si el servicio no responde o no tiene el codigo, usa el respaldo (el archivo de la API). Tras una falla
 * espera 30 segundos antes de volver a intentar, para no demorar cada peticion mientras el servicio despierta.
 */
public class CatalogoMensajesServicioAdaptador implements CatalogoMensajesPuertoSalida {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogoMensajesServicioAdaptador.class);
	private static final Locale IDIOMA = Locale.forLanguageTag("es-CO");
	private static final Duration PAUSA_TRAS_FALLA = Duration.ofSeconds(30);

	private final ClientePlantillasMensajes cliente;
	private final CatalogoMensajesPuertoSalida respaldo;
	private final Clock reloj;
	private volatile Instant reintentarDesde = Instant.MIN;

	public CatalogoMensajesServicioAdaptador(final ClientePlantillasMensajes cliente,
			final CatalogoMensajesPuertoSalida respaldo, final Clock reloj) {
		this.cliente = cliente;
		this.respaldo = respaldo;
		this.reloj = reloj;
	}

	@Override
	public MensajeCatalogoDTO consultarMensaje(final CodigoMensaje codigo, final Object... parametros) {
		var plantilla = consultarPlantilla(codigo);
		if (plantilla == null) {
			return respaldo.consultarMensaje(codigo, parametros);
		}
		return new MensajeCatalogoDTO(codigo, formatear(plantilla, parametros));
	}

	private String consultarPlantilla(final CodigoMensaje codigo) {
		if (reloj.instant().isBefore(reintentarDesde)) {
			return null;
		}
		try {
			return cliente.consultarPlantilla(codigo.name());
		} catch (RuntimeException excepcion) {
			reintentarDesde = reloj.instant().plus(PAUSA_TRAS_FALLA);
			LOGGER.warn("El catálogo de mensajes no respondió, se usa el respaldo: {}", excepcion.getMessage());
			return null;
		}
	}

	private static String formatear(final String plantilla, final Object... parametros) {
		if (parametros == null || parametros.length == 0) {
			return plantilla;
		}
		var textos = new Object[parametros.length];
		for (int i = 0; i < parametros.length; i++) {
			textos[i] = String.valueOf(parametros[i]);
		}
		return new MessageFormat(plantilla, IDIOMA).format(textos);
	}

}
