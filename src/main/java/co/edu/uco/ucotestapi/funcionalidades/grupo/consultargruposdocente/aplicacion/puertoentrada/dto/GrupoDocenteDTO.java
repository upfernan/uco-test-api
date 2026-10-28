package co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.dto;

import java.time.LocalDate;
import java.util.UUID;

public record GrupoDocenteDTO(UUID id, String nombre, String estado, LocalDate fechaInicio, LocalDate fechaFin) {

}
