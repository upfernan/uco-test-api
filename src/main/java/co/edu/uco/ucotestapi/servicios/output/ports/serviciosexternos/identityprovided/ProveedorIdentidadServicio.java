package co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided;

public interface ProveedorIdentidadServicio {

	String cifrarClave(String clave);

	boolean claveCoincide(String clave, String claveCifrada);

	TokenDTO emitirToken(DatosSesionDTO datosSesion);

}