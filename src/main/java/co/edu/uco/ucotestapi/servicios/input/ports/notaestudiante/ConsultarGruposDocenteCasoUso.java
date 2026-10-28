package co.edu.uco.ucotestapi.servicios.input.ports.notaestudiante;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.input.ports.CasoUsoConRetorno;
import co.edu.uco.ucotestapi.servicios.input.dto.response.GrupoResponseDTO;

public interface ConsultarGruposDocenteCasoUso extends CasoUsoConRetorno<List<GrupoResponseDTO>, UUID> {

}
