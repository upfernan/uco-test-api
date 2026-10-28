package co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoGrupo;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.GrupoRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.GrupoEntidad;
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
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.GRUPO_ID_NULO);
		}
		if (!grupoRepositorio.existePorId(id)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.GRUPO_NO_EXISTE, id);
		}
	}

	// Regla 3
	public void validarGrupoVigente() {
		var grupo = obtenerGrupo();
		if (grupo.getEstado() != EstadoGrupo.ACTIVO) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.GRUPO_NO_ACTIVO, id);
		}
		var hoy = LocalDate.now();
		if (UtilFecha.esNula(grupo.getFechaInicio()) || UtilFecha.esNula(grupo.getFechaFin())
				|| hoy.isBefore(grupo.getFechaInicio()) || hoy.isAfter(grupo.getFechaFin())) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.GRUPO_NO_VIGENTE, id);
		}
	}

	// Regla 2
	public void validarDocenteAsignado(UUID idDocente) {
		var docente = obtenerGrupo().getDocente();
		if (UtilUUID.esValorDefecto(idDocente) || UtilObjeto.esNulo(docente) || !idDocente.equals(docente.getId())) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.DOCENTE_NO_ASIGNADO_AL_GRUPO, idDocente, id);
		}
	}

	private GrupoEntidad obtenerGrupo() {
		validarGrupoExista();
		return grupoRepositorio.consultarPorId(id).orElseThrow(
				() -> CatalogoMensajesProveedor.excepcion(CodigoMensaje.GRUPO_NO_EXISTE, id));
	}

}