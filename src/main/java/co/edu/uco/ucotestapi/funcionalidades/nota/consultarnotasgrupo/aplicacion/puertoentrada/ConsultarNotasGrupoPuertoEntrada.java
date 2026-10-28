package co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.PuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.dto.NotaGrupoDTO;

public interface ConsultarNotasGrupoPuertoEntrada extends PuertoEntrada<UUID, List<NotaGrupoDTO>> {

}
