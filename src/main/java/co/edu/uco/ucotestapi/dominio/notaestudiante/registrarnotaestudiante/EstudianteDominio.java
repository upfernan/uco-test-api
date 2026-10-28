package co.edu.uco.ucotestapi.dominio.notaestudiante.registrarnotaestudiante;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteRepositorio;
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
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.ESTUDIANTE_ID_NULO);
		}
		if (!estudianteRepositorio.existePorId(id)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.ESTUDIANTE_NO_EXISTE, id);
		}
	}
	private void ValidarEstudianteEsteActivo() {
		if (UtilUUID.esValorDefecto(id)) {
			throw CatalogoMensajesProveedor.excepcion(CodigoMensaje.ESTUDIANTE_ID_NULO);
		}
	
	}

}