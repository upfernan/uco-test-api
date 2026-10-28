package co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad;

import java.util.UUID;

import co.edu.uco.ucotestapi.transversal.enumeracion.RolUsuario;

public record DatosSesionDTO(UUID idUsuario, String correo, String nombre, RolUsuario rol, UUID idPersona) {

}