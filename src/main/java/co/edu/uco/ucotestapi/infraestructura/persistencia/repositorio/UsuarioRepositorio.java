package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ModificarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.CrearRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.EliminarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ConsultarRepositorio;

import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;

public interface UsuarioRepositorio extends CrearRepositorio<UsuarioEntidad>, ConsultarRepositorio<UUID, UsuarioEntidad>, ModificarRepositorio<UUID, UsuarioEntidad>, EliminarRepositorio<UUID> {

	boolean existePorCorreo(String correo);

	Optional<UsuarioEntidad> consultarPorCorreo(String correo);

}