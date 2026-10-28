package co.edu.uco.ucotestapi.servicios.input.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record GrupoResponseDTO(UUID id, String nombre, String estado, LocalDate fechaInicio, LocalDate fechaFin) {

}
