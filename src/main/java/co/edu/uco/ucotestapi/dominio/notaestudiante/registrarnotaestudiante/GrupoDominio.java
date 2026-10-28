package co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.dominio.enums.EstadoGrupo;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.GrupoEntidad;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilFecha;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public class GrupoDominio {
	private UUID id;
	private GrupoRepositorio grupoRepositorio;

	public GrupoDominio(UUID id, GrupoRepositorio grupoRepositorio) {
		this.id = UtilUUID.obtenerValorDefecto(id);
		this.grupoRepositorio = Objects.requireNonNull(grupoRepositorio, "El repositorio de grupos es obligatorio");
	}

	// Regla 8
	public void validarGrupoExista() {
		if (UtilUUID.esValorDefecto(id)) {
			throw new ReglaNegocioException(CodigoMensaje.GRUPO_ID_NULO);
		}
		if (!grupoRepositorio.existePorId(id)) {
			throw new ReglaNegocioException(CodigoMensaje.GRUPO_NO_EXISTE, id);
		}
	}

	// Regla 3
	public void validarGrupoVigente() {
		var grupo = obtenerGrupo();
		if (grupo.getEstado() != EstadoGrupo.ACTIVO) {
			throw new ReglaNegocioException(CodigoMensaje.GRUPO_NO_ACTIVO, id);
		}
		var hoy = LocalDate.now();
		if (UtilFecha.esNula(grupo.getFechaInicio()) || UtilFecha.esNula(grupo.getFechaFin())
				|| hoy.isBefore(grupo.getFechaInicio()) || hoy.isAfter(grupo.getFechaFin())) {
			throw new ReglaNegocioException(CodigoMensaje.GRUPO_NO_VIGENTE, id);
		}
	}

	// Regla 2
	public void validarDocenteAsignado(UUID idDocente) {
		var docente = obtenerGrupo().getDocente();
		if (UtilUUID.esValorDefecto(idDocente) || UtilObjeto.esNulo(docente) || !idDocente.equals(docente.getId())) {
			throw new ReglaNegocioException(CodigoMensaje.DOCENTE_NO_ASIGNADO_AL_GRUPO, idDocente, id);
		}
	}

	private GrupoEntidad obtenerGrupo() {
		validarGrupoExista();
		return grupoRepositorio.consultarPorId(id).orElseThrow(
				() -> new ReglaNegocioException(CodigoMensaje.GRUPO_NO_EXISTE, id));
	}

}