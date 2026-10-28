package co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RegistrarNotaEstudianteDTO(
		UUID idDocente,
		UUID idGrupo,
		UUID idEstudiante,
		UUID idNota,
		BigDecimal valor) {

}
