package co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoDocente;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.DocenteRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.DocenteEntidad;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public class DocenteDominio {
	private UUID id;
	private DocenteRepositorio docenteRepositorio;

	public DocenteDominio(UUID id, DocenteRepositorio docenteRepositorio) {
		this.id = UtilUUID.obtenerValorDefecto(id);
		this.docenteRepositorio = Objects.requireNonNull(docenteRepositorio, "El repositorio de docentes es obligatorio");
	}

	public void validarDocenteExista() {
		if (UtilUUID.esValorDefecto(id)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.DOCENTE_ID_NULO);
		}
		if (!docenteRepositorio.existePorId(id)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.DOCENTE_NO_EXISTE, id);
		}
	}

	// Regla 7
	public void validarDocenteActivo() {
		if (obtenerDocente().getEstado() != EstadoDocente.ACTIVO) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.DOCENTE_NO_ACTIVO, id);
		}
	}

	private DocenteEntidad obtenerDocente() {
		validarDocenteExista();
		return docenteRepositorio.consultarPorId(id).orElseThrow(
				() -> CatalogoMensajesProveedor.excepcion(CodigoMensaje.DOCENTE_NO_EXISTE, id));
	}

}