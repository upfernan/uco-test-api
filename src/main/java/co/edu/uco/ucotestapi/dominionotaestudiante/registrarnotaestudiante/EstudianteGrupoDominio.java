package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoEstudianteRepositorio;

public class EstudianteGrupoDominio {
	private UUID idEstudiante;
	private UUID idGrupo;
	private GrupoEstudianteRepositorio grupoEstudianteRepositorio;

	public EstudianteGrupoDominio(UUID idEstudiante, UUID idGrupo, GrupoEstudianteRepositorio grupoEstudianteRepositorio) {
		this.idEstudiante = idEstudiante;
		this.idGrupo = idGrupo;
		this.grupoEstudianteRepositorio = Objects.requireNonNull(grupoEstudianteRepositorio, "El repositorio de estudiantes por grupo es obligatorio");
	}

	// Regla 1
	public void validarEstudianteExistaEnGrupo() {
		if (idEstudiante == null || idGrupo == null) {
			throw new IllegalArgumentException("El ID del estudiante y el ID del grupo no pueden ser nulos");
		}
		if (!grupoEstudianteRepositorio.existePorEstudianteYGrupo(idEstudiante, idGrupo)) {
			throw new IllegalArgumentException("El estudiante con ID " + idEstudiante + " no pertenece al grupo " + idGrupo);
		}
	}

}