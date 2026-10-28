package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.dominio;

import java.util.UUID;

import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;

public record CuentaRegistradaDominio(UUID id, String correo, RolUsuario rol, UUID idPersona) {

}
