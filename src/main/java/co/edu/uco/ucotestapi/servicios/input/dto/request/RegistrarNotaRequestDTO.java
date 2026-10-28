package co.edu.uco.ucotestapi.servicios.input.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Lo que envia la pantalla o Bruno. El docente no viaja aqui: se toma del token de la sesion.
 */
public record RegistrarNotaRequestDTO(UUID idGrupo, UUID idEstudiante, UUID idNota, BigDecimal valor) {

}
