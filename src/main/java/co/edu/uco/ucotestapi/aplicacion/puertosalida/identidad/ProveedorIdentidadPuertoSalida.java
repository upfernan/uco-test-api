package co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad;

public interface ProveedorIdentidadPuertoSalida {

	String cifrarClave(String clave);

	boolean claveCoincide(String clave, String claveCifrada);

	TokenDTO emitirToken(DatosSesionDTO datosSesion);

}