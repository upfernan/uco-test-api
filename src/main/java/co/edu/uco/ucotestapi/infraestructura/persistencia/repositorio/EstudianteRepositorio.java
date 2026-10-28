package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ModificarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.CrearRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.EliminarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ConsultarRepositorio;

import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteEntidad;

public interface EstudianteRepositorio extends CrearRepositorio<EstudianteEntidad>, ConsultarRepositorio<UUID, EstudianteEntidad>, ModificarRepositorio<UUID, EstudianteEntidad>, EliminarRepositorio<UUID> {

	Optional<EstudianteEntidad> consultarPorCorreo(String correo);

}
