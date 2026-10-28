package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.notificaciones;

import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertosalida.CodigoRecuperacionPuertoSalida;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertosalida.NotificacionPuertoSalida;

/**
 * Un mismo adaptador atiende los dos puertos que envian correos: el aviso de nota registrada
 * y el codigo de recuperacion de contrasena. Asi Spring tiene un solo bean para ambos.
 */
public interface NotificadorCorreo extends NotificacionPuertoSalida, CodigoRecuperacionPuertoSalida {

}
