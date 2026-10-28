package co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.consultas;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.casosuso.CasoUsoConRetorno;
import co.edu.uco.ucotestapi.servicios.input.dto.response.NotaResponseDTO;

public interface ConsultarNotasGrupoCasoUso extends CasoUsoConRetorno<List<NotaResponseDTO>, UUID> {

}
