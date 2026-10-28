package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.UsuarioEntidad;

public interface UsuarioRepositorio extends CrearRepositorio<UsuarioEntidad>, ConsultarRepositorio<UUID, UsuarioEntidad>, ModificarRepositorio<UUID, UsuarioEntidad>, EliminarRepositorio<UUID> {

	boolean existePorCorreo(String correo);

	Optional<UsuarioEntidad> consultarPorCorreo(String correo);

}