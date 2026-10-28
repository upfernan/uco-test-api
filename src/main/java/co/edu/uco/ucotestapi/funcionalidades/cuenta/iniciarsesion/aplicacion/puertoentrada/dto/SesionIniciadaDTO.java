package co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.dto;

import java.time.Instant;
import java.util.UUID;

public record SesionIniciadaDTO(String token, Instant expiraEn, String rol, String nombre, UUID idPersona) {

}
