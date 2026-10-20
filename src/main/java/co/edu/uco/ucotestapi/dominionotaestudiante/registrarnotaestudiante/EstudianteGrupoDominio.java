package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoEstudianteRepositorio;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public class EstudianteGrupoDominio {
	private UUID idEstudiante;
	private UUID idGrupo;
	private GrupoEstudianteRepositorio grupoEstudianteRepositorio;

	public EstudianteGrupoDominio(UUID idEstudiante, UUID idGrupo, GrupoEstudianteRepositorio grupoEstudianteRepositorio) {
		this.idEstudiante = UtilUUID.obtenerValorDefecto(idEstudiante);
		this.idGrupo = UtilUUID.obtenerValorDefecto(idGrupo);
		this.grupoEstudianteRepositorio = Objects.requireNonNull(grupoEstudianteRepositorio, "El repositorio de estudiantes por grupo es obligatorio");
	}

	// Regla 1
	public void validarEstudianteExistaEnGrupo() {
		if (UtilUUID.esValorDefecto(idEstudiante) || UtilUUID.esValorDefecto(idGrupo)) {
			throw new ReglaNegocioException(CodigoMensaje.ESTUDIANTE_GRUPO_IDS_NULOS);
		}
		if (!grupoEstudianteRepositorio.existePorEstudianteYGrupo(idEstudiante, idGrupo)) {
			throw new ReglaNegocioException(CodigoMensaje.ESTUDIANTE_NO_PERTENECE_AL_GRUPO, idEstudiante, idGrupo);
		}
	}

}