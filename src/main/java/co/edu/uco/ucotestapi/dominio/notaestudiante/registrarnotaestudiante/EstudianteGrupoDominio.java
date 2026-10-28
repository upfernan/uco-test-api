package co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteGrupoRepositorio;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public class EstudianteGrupoDominio {
	private UUID idEstudiante;
	private UUID idGrupo;
	private EstudianteGrupoRepositorio grupoEstudianteRepositorio;

	public EstudianteGrupoDominio(UUID idEstudiante, UUID idGrupo, EstudianteGrupoRepositorio grupoEstudianteRepositorio) {
		this.idEstudiante = UtilUUID.obtenerValorDefecto(idEstudiante);
		this.idGrupo = UtilUUID.obtenerValorDefecto(idGrupo);
		this.grupoEstudianteRepositorio = Objects.requireNonNull(grupoEstudianteRepositorio, "El repositorio de estudiantes por grupo es obligatorio");
	}

	// Regla 1
	public void validarEstudianteExistaEnGrupo() {
		if (UtilUUID.esValorDefecto(idEstudiante) || UtilUUID.esValorDefecto(idGrupo)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.ESTUDIANTE_GRUPO_IDS_NULOS);
		}
		if (!grupoEstudianteRepositorio.existePorEstudianteYGrupo(idEstudiante, idGrupo)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.ESTUDIANTE_NO_PERTENECE_AL_GRUPO, idEstudiante, idGrupo);
		}
	}

}