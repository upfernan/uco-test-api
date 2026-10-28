package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad;

import java.util.UUID;

import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoDocente;

public class DocenteEntidad {
	private UUID id;
	private String nombre;
	private String correo;
	private EstadoDocente estado;

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


	public EstadoDocente getEstado() {
		return estado;
	}

	public void setEstado(EstadoDocente estado) {
		this.estado = estado;
	}

}
