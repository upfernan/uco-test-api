package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.GrupoEntidad;

public interface GrupoRepositorio extends CrearRepositorio<GrupoEntidad>, ConsultarRepositorio<UUID, GrupoEntidad>, ModificarRepositorio<UUID, GrupoEntidad>, EliminarRepositorio<UUID> {

	List<GrupoEntidad> consultarPorDocente(UUID idDocente);

}