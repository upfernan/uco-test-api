package co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.dto;

import java.time.Instant;
import java.util.UUID;

public record NotaGrupoDTO(UUID id, String nombre, String estado, Instant fechaRegistroDesde, Instant fechaRegistroHasta) {

}
