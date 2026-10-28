package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ModificarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.CrearRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.EliminarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ConsultarRepositorio;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;

public interface GrupoRepositorio extends CrearRepositorio<GrupoEntidad>, ConsultarRepositorio<UUID, GrupoEntidad>, ModificarRepositorio<UUID, GrupoEntidad>, EliminarRepositorio<UUID> {

	List<GrupoEntidad> consultarPorDocente(UUID idDocente);

}