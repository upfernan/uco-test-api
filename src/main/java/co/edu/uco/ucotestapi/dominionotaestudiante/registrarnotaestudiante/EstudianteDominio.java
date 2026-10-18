package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.EstudianteRepositorio;

public class EstudianteDominio {
	private UUID id;
	private EstudianteRepositorio estudianteRepositorio;

	public EstudianteDominio(UUID id, EstudianteRepositorio estudianteRepositorio) {
		this.id = id;
		this.estudianteRepositorio = Objects.requireNonNull(estudianteRepositorio, "El repositorio de estudiantes es obligatorio");
	}

	public void validarEstudianteExista() {
		if (id == null) {
			throw new IllegalArgumentException("El ID del estudiante no puede ser nulo");
		}
		if (!estudianteRepositorio.existePorId(id)) {
			throw new IllegalArgumentException("El estudiante con ID " + id + " no existe");
		}
	}
	private void ValidarEstudianteEsteActivo() {
		if (estudianteRepositorio == null) {
			throw new IllegalArgumentException("El ID del estudiante no puede ser nulo");
		}
	
	}

}