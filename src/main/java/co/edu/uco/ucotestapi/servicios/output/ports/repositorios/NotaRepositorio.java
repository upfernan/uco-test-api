package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEntidad;

public interface NotaRepositorio extends CrearRepositorio<NotaEntidad>, ConsultarRepositorio<UUID, NotaEntidad>, ModificarRepositorio<UUID, NotaEntidad>, EliminarRepositorio<UUID> {

	List<NotaEntidad> consultarPorGrupo(UUID idGrupo);

}