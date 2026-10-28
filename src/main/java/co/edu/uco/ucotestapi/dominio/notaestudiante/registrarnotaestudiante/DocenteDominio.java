package co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.dominio.enums.EstadoDocente;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.DocenteRepositorio;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;
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
			throw new ReglaNegocioException(CodigoMensaje.DOCENTE_ID_NULO);
		}
		if (!docenteRepositorio.existePorId(id)) {
			throw new ReglaNegocioException(CodigoMensaje.DOCENTE_NO_EXISTE, id);
		}
	}

	// Regla 7
	public void validarDocenteActivo() {
		if (obtenerDocente().getEstado() != EstadoDocente.ACTIVO) {
			throw new ReglaNegocioException(CodigoMensaje.DOCENTE_NO_ACTIVO, id);
		}
	}

	private DocenteEntidad obtenerDocente() {
		validarDocenteExista();
		return docenteRepositorio.consultarPorId(id).orElseThrow(
				() -> new ReglaNegocioException(CodigoMensaje.DOCENTE_NO_EXISTE, id));
	}

}