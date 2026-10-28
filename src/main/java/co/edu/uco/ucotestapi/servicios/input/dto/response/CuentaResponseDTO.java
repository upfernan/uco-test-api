package co.edu.uco.ucotestapi.servicios.input.dto.response;

import java.util.UUID;

public record CuentaResponseDTO(UUID id, String correo, String rol, UUID idPersona) {

}
