package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.EstadoGrupo;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.GrupoEntidad;

public class GrupoDominio {
	private UUID id;
	private GrupoRepositorio grupoRepositorio;

	public GrupoDominio(UUID id, GrupoRepositorio grupoRepositorio) {
		this.id = id;
		this.grupoRepositorio = Objects.requireNonNull(grupoRepositorio, "El repositorio de grupos es obligatorio");
	}

	// Regla 8
	public void validarGrupoExista() {
		if (id == null) {
			throw new IllegalArgumentException("El ID del grupo no puede ser nulo");
		}
		if (!grupoRepositorio.existePorId(id)) {
			throw new IllegalArgumentException("El grupo con ID " + id + " no existe");
		}
	}

	// Regla 3
	public void validarGrupoVigente() {
		var grupo = obtenerGrupo();
		if (grupo.getEstado() != EstadoGrupo.ACTIVO) {
			throw new IllegalArgumentException("El grupo con ID " + id + " no esta activo");
		}
		var hoy = LocalDate.now();
		if (grupo.getFechaInicio() == null || grupo.getFechaFin() == null
				|| hoy.isBefore(grupo.getFechaInicio()) || hoy.isAfter(grupo.getFechaFin())) {
			throw new IllegalArgumentException("El grupo con ID " + id + " no esta vigente");
		}
	}

	// Regla 2
	public void validarDocenteAsignado(UUID idDocente) {
		var docente = obtenerGrupo().getDocente();
		if (idDocente == null || docente == null || !idDocente.equals(docente.getId())) {
			throw new IllegalArgumentException("El docente con ID " + idDocente + " no esta asignado al grupo " + id);
		}
	}

	private GrupoEntidad obtenerGrupo() {
		validarGrupoExista();
		return grupoRepositorio.consultarPorId(id).orElseThrow(
				() -> new IllegalArgumentException("El grupo con ID " + id + " no existe"));
	}

}