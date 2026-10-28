package co.edu.uco.ucotestapi.servicios.input.dto.response;

import java.time.Instant;
import java.util.UUID;

public record NotaResponseDTO(UUID id, String nombre, String estado, Instant fechaRegistroDesde, Instant fechaRegistroHasta) {

}
