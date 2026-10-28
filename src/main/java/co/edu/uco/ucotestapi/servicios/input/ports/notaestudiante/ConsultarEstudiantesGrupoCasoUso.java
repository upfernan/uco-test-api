package co.edu.uco.ucotestapi.servicios.input.ports.notaestudiante;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.input.ports.CasoUsoConRetorno;
import co.edu.uco.ucotestapi.servicios.input.dto.response.EstudianteResponseDTO;

public interface ConsultarEstudiantesGrupoCasoUso extends CasoUsoConRetorno<List<EstudianteResponseDTO>, UUID> {

}
