package co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.PuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.dto.GrupoDocenteDTO;

public interface ConsultarGruposDocentePuertoEntrada extends PuertoEntrada<UUID, List<GrupoDocenteDTO>> {

}
