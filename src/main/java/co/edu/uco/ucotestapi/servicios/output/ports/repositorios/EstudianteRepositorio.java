package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteEntidad;

public interface EstudianteRepositorio extends CrearRepositorio<EstudianteEntidad>, ConsultarRepositorio<UUID, EstudianteEntidad>, ModificarRepositorio<UUID, EstudianteEntidad>, EliminarRepositorio<UUID> {

	Optional<EstudianteEntidad> consultarPorCorreo(String correo);

}
