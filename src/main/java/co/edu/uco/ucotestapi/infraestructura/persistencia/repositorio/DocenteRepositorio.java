package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ModificarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.CrearRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.EliminarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ConsultarRepositorio;

import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;

public interface DocenteRepositorio extends CrearRepositorio<DocenteEntidad>, ConsultarRepositorio<UUID, DocenteEntidad>, ModificarRepositorio<UUID, DocenteEntidad>, EliminarRepositorio<UUID> {

	Optional<DocenteEntidad> consultarPorCorreo(String correo);

}