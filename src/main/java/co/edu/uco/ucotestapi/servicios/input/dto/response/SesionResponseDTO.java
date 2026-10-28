package co.edu.uco.ucotestapi.servicios.input.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SesionResponseDTO(String token, Instant expiraEn, String rol, String nombre, UUID idPersona) {

}
