package co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades;

import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public class UsuarioEntidad {
	private UUID id;
	private String correo;
	private String claveHash;
	private RolUsuario rol;
	private UUID idDocente = UtilUUID.UUID_DEFECTO;
	private UUID idEstudiante = UtilUUID.UUID_DEFECTO;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getClaveHash() {
		return claveHash;
	}

	public void setClaveHash(String claveHash) {
		this.claveHash = claveHash;
	}

	public RolUsuario getRol() {
		return rol;
	}

	public void setRol(RolUsuario rol) {
		this.rol = rol;
	}

	public UUID getIdDocente() {
		return idDocente;
	}

	public void setIdDocente(UUID idDocente) {
		this.idDocente = UtilUUID.obtenerValorDefecto(idDocente);
	}

	public UUID getIdEstudiante() {
		return idEstudiante;
	}

	public void setIdEstudiante(UUID idEstudiante) {
		this.idEstudiante = UtilUUID.obtenerValorDefecto(idEstudiante);
	}

	public UUID getIdPersona() {
		return rol == RolUsuario.DOCENTE ? idDocente : idEstudiante;
	}

}