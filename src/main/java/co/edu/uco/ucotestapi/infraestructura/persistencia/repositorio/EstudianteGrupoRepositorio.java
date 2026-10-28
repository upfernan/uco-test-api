package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ModificarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.CrearRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.EliminarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ConsultarRepositorio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;

public interface EstudianteGrupoRepositorio extends CrearRepositorio<EstudianteGrupoEntidad>, ConsultarRepositorio<UUID, EstudianteGrupoEntidad>, ModificarRepositorio<UUID, EstudianteGrupoEntidad>, EliminarRepositorio<UUID> {

	boolean existePorEstudianteYGrupo(UUID idEstudiante, UUID idGrupo);

	Optional<EstudianteGrupoEntidad> consultarPorEstudianteYGrupo(UUID idEstudiante, UUID idGrupo);

	List<EstudianteGrupoEntidad> consultarPorGrupo(UUID idGrupo);

}