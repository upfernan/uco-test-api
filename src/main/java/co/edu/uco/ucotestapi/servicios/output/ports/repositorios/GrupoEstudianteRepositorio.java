package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.EstudianteGrupoEntidad;

public interface GrupoEstudianteRepositorio extends CrearRepositorio<EstudianteGrupoEntidad>, ConsultarRepositorio<UUID, EstudianteGrupoEntidad>, ActualizarRepositorio<EstudianteGrupoEntidad>, EliminarRepositorio<UUID> {

	boolean existePorEstudianteYGrupo(UUID idEstudiante, UUID idGrupo);

}