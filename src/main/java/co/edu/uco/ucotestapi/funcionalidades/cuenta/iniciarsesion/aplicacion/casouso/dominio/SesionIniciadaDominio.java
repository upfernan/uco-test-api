package co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.dominio;

import java.time.Instant;
import java.util.UUID;

import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;

public record SesionIniciadaDominio(String token, Instant expiraEn, RolUsuario rol, String nombre, UUID idPersona) {

}
