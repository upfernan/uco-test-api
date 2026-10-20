package co.edu.uco.ucotestapi.servicios.input.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record RegistrarNotaEstudianteDTO(
		UUID idDocente,
		UUID idGrupo,
		UUID idEstudiante,
		UUID idNota,
		BigDecimal valor) {

}
