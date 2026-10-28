package co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades;

import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public record UsuarioEntidad(UUID id, String correo, String claveHash, RolUsuario rol, UUID idDocente, UUID idEstudiante) {

	public UsuarioEntidad {
		idDocente = UtilUUID.obtenerValorDefecto(idDocente);
		idEstudiante = UtilUUID.obtenerValorDefecto(idEstudiante);
	}

	public static UsuarioEntidad deDocente(UUID id, String correo, String claveHash, UUID idDocente) {
		return new UsuarioEntidad(id, correo, claveHash, RolUsuario.DOCENTE, idDocente, UtilUUID.UUID_DEFECTO);
	}

	public static UsuarioEntidad deEstudiante(UUID id, String correo, String claveHash, UUID idEstudiante) {
		return new UsuarioEntidad(id, correo, claveHash, RolUsuario.ESTUDIANTE, UtilUUID.UUID_DEFECTO, idEstudiante);
	}

	public UUID idPersona() {
		return rol == RolUsuario.DOCENTE ? idDocente : idEstudiante;
	}

}
