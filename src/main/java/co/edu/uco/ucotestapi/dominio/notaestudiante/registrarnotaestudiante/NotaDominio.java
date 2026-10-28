package co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoNota;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilFecha;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public class NotaDominio {
	private UUID id;
	private NotaRepositorio notaRepositorio;

	public NotaDominio(UUID id, NotaRepositorio notaRepositorio) {
		this.id = UtilUUID.obtenerValorDefecto(id);
		this.notaRepositorio = Objects.requireNonNull(notaRepositorio, "El repositorio de notas es obligatorio");
	}

	// Regla 4
	public void validarNotaExista() {
		if (UtilUUID.esValorDefecto(id)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.NOTA_ID_NULO);
		}
		if (!notaRepositorio.existePorId(id)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.NOTA_NO_EXISTE, id);
		}
	}

	// Regla 13
	public void validarNotaActiva() {
		if (obtenerNota().getEstado() != EstadoNota.ACTIVO) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.NOTA_NO_ACTIVA, id);
		}
	}

	// Regla 5
	public void validarFechaRegistroVigente() {
		var nota = obtenerNota();
		var ahora = Instant.now();
		if (UtilFecha.esNulo(nota.getFechaRegistroDesde()) || UtilFecha.esNulo(nota.getFechaRegistroHasta())
				|| ahora.isBefore(nota.getFechaRegistroDesde()) || ahora.isAfter(nota.getFechaRegistroHasta())) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.NOTA_FECHA_REGISTRO_NO_VIGENTE, id);
		}
	}

	// Regla 12
	public void validarNotaPertenezcaAlGrupo(UUID idGrupo) {
		var grupo = obtenerNota().getGrupo();
		if (UtilUUID.esValorDefecto(idGrupo) || UtilObjeto.esNulo(grupo) || !idGrupo.equals(grupo.getId())) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.NOTA_NO_PERTENECE_AL_GRUPO, id, idGrupo);
		}
	}

	private NotaEntidad obtenerNota() {
		validarNotaExista();
		return notaRepositorio.consultarPorId(id).orElseThrow(
				() -> CatalogoMensajesProveedor.excepcion(CodigoMensaje.NOTA_NO_EXISTE, id));
	}

}