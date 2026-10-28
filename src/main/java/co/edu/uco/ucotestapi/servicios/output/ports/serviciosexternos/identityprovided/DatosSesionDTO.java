package co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided;

import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;

public record DatosSesionDTO(UUID idUsuario, String correo, String nombre, RolUsuario rol, UUID idPersona) {

}