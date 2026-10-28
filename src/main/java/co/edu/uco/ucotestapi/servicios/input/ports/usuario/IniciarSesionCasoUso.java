package co.edu.uco.ucotestapi.servicios.input.ports.usuario;

import co.edu.uco.ucotestapi.servicios.input.ports.CasoUsoConRetorno;
import co.edu.uco.ucotestapi.servicios.input.dto.request.IniciarSesionDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.response.SesionResponseDTO;

public interface IniciarSesionCasoUso extends CasoUsoConRetorno<SesionResponseDTO, IniciarSesionDTO> {

}
