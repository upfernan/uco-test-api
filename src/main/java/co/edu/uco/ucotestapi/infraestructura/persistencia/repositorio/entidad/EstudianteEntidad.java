package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad;

import java.util.UUID;

public class EstudianteEntidad {
	private UUID id;
	private String nombre;
	private String correo;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}


}