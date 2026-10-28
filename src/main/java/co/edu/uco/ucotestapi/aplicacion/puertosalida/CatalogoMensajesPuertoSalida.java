package co.edu.uco.ucotestapi.aplicacion.puertosalida;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.MensajeCatalogoDTO;

public interface CatalogoMensajesPuertoSalida {

	MensajeCatalogoDTO consultarMensaje(CodigoMensaje codigo, Object... parametros);

}