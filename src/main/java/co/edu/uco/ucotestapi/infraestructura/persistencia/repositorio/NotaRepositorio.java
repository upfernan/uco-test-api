package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ModificarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.CrearRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.EliminarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ConsultarRepositorio;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;

public interface NotaRepositorio extends CrearRepositorio<NotaEntidad>, ConsultarRepositorio<UUID, NotaEntidad>, ModificarRepositorio<UUID, NotaEntidad>, EliminarRepositorio<UUID> {

	List<NotaEntidad> consultarPorGrupo(UUID idGrupo);

}