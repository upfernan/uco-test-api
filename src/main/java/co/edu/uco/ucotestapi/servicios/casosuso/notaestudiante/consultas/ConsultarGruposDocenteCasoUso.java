package co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.consultas;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.casosuso.CasoUsoConRetorno;
import co.edu.uco.ucotestapi.servicios.input.dto.response.GrupoResponseDTO;

public interface ConsultarGruposDocenteCasoUso extends CasoUsoConRetorno<List<GrupoResponseDTO>, UUID> {

}
