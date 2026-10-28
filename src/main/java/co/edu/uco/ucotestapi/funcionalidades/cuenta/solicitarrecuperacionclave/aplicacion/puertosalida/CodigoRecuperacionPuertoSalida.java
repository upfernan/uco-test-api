package co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertosalida;

/** Envia al usuario el codigo para recuperar la contrasena (por correo en la nube, al log en local). */
public interface CodigoRecuperacionPuertoSalida {

	void enviar(CodigoRecuperacionDTO codigo);

}
