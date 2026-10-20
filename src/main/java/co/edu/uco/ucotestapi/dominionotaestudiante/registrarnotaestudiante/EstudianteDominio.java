package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.EstudianteRepositorio;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public class EstudianteDominio {
	private UUID id;
	private EstudianteRepositorio estudianteRepositorio;

	public EstudianteDominio(UUID id, EstudianteRepositorio estudianteRepositorio) {
		this.id = UtilUUID.obtenerValorDefecto(id);
		this.estudianteRepositorio = Objects.requireNonNull(estudianteRepositorio, "El repositorio de estudiantes es obligatorio");
	}

	public void validarEstudianteExista() {
		if (UtilUUID.esValorDefecto(id)) {
			throw new ReglaNegocioException(CodigoMensaje.ESTUDIANTE_ID_NULO);
		}
		if (!estudianteRepositorio.existePorId(id)) {
			throw new ReglaNegocioException(CodigoMensaje.ESTUDIANTE_NO_EXISTE, id);
		}
	}
	private void ValidarEstudianteEsteActivo() {
		if (UtilUUID.esValorDefecto(id)) {
			throw new ReglaNegocioException(CodigoMensaje.ESTUDIANTE_ID_NULO);
		}
	
	}

}