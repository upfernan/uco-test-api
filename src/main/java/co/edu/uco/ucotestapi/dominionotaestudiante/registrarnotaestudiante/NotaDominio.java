package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.EstadoNota;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEntidad;

public class NotaDominio {
	private UUID id;
	private NotaRepositorio notaRepositorio;

	public NotaDominio(UUID id, NotaRepositorio notaRepositorio) {
		this.id = id;
		this.notaRepositorio = Objects.requireNonNull(notaRepositorio, "El repositorio de notas es obligatorio");
	}

	// Regla 4
	public void validarNotaExista() {
		if (id == null) {
			throw new IllegalArgumentException("El ID de la nota no puede ser nulo");
		}
		if (!notaRepositorio.existePorId(id)) {
			throw new IllegalArgumentException("La nota con ID " + id + " no existe");
		}
	}

	// Regla 13
	public void validarNotaActiva() {
		if (obtenerNota().getEstado() != EstadoNota.ACTIVO) {
			throw new IllegalArgumentException("La nota con ID " + id + " no esta activa");
		}
	}

	// Regla 5
	public void validarFechaRegistroVigente() {
		var nota = obtenerNota();
		var ahora = Instant.now();
		if (nota.getFechaRegistroDesde() == null || nota.getFechaRegistroHasta() == null
				|| ahora.isBefore(nota.getFechaRegistroDesde()) || ahora.isAfter(nota.getFechaRegistroHasta())) {
			throw new IllegalArgumentException("La fecha para registrar la nota con ID " + id + " no esta vigente");
		}
	}

	// Regla 12
	public void validarNotaPertenezcaAlGrupo(UUID idGrupo) {
		var grupo = obtenerNota().getGrupo();
		if (idGrupo == null || grupo == null || !idGrupo.equals(grupo.getId())) {
			throw new IllegalArgumentException("La nota con ID " + id + " no pertenece al grupo " + idGrupo);
		}
	}

	private NotaEntidad obtenerNota() {
		validarNotaExista();
		return notaRepositorio.consultarPorId(id).orElseThrow(
				() -> new IllegalArgumentException("La nota con ID " + id + " no existe"));
	}

}