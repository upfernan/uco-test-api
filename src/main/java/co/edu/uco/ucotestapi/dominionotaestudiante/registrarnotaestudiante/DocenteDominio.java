package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.EstadoDocente;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.DocenteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;

public class DocenteDominio {
	private UUID id;
	private DocenteRepositorio docenteRepositorio;

	public DocenteDominio(UUID id, DocenteRepositorio docenteRepositorio) {
		this.id = id;
		this.docenteRepositorio = Objects.requireNonNull(docenteRepositorio, "El repositorio de docentes es obligatorio");
	}

	public void validarDocenteExista() {
		if (id == null) {
			throw new IllegalArgumentException("El ID del docente no puede ser nulo");
		}
		if (!docenteRepositorio.existePorId(id)) {
			throw new IllegalArgumentException("El docente con ID " + id + " no existe");
		}
	}

	// Regla 7
	public void validarDocenteActivo() {
		if (obtenerDocente().getEstado() != EstadoDocente.ACTIVO) {
			throw new IllegalArgumentException("El docente con ID " + id + " no esta activo");
		}
	}

	private DocenteEntidad obtenerDocente() {
		validarDocenteExista();
		return docenteRepositorio.consultarPorId(id).orElseThrow(
				() -> new IllegalArgumentException("El docente con ID " + id + " no existe"));
	}

}