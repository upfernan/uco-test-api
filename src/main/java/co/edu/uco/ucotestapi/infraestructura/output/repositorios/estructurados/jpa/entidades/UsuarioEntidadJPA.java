package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades;

import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.RolUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "Usuario")
public class UsuarioEntidadJPA {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "correo")
	private String correo;

	@Column(name = "clave_hash")
	private String claveHash;

	@Enumerated(EnumType.STRING)
	@Column(name = "rol")
	private RolUsuario rol;

	@Column(name = "id_docente")
	private UUID idDocente;

	@Column(name = "id_estudiante")
	private UUID idEstudiante;

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
		this.idDocente = idDocente;
	}

	public UUID getIdEstudiante() {
		return idEstudiante;
	}

	public void setIdEstudiante(UUID idEstudiante) {
		this.idEstudiante = idEstudiante;
	}

}