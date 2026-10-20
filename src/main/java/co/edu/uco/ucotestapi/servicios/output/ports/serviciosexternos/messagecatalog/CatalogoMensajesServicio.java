package co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog;

public interface CatalogoMensajesServicio {

	MensajeDTO consultarMensaje(CodigoMensaje codigo, Object... parametros);

}