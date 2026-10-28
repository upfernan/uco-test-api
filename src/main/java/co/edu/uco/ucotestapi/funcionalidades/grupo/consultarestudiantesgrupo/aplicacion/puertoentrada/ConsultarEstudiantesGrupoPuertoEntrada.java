package co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.PuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.dto.EstudianteGrupoDTO;

public interface ConsultarEstudiantesGrupoPuertoEntrada extends PuertoEntrada<UUID, List<EstudianteGrupoDTO>> {

}
