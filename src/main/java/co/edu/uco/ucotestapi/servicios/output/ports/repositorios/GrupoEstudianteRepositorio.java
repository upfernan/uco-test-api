package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteGrupoEntidad;

public interface GrupoEstudianteRepositorio extends CrearRepositorio<EstudianteGrupoEntidad>, ConsultarRepositorio<UUID, EstudianteGrupoEntidad>, ModificarRepositorio<UUID, EstudianteGrupoEntidad>, EliminarRepositorio<UUID> {

	boolean existePorEstudianteYGrupo(UUID idEstudiante, UUID idGrupo);

}