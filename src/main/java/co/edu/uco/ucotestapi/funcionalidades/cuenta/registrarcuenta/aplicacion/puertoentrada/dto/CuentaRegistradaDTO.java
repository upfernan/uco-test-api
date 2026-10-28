package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.dto;

import java.util.UUID;

public record CuentaRegistradaDTO(UUID id, String correo, String rol, UUID idPersona) {

}
