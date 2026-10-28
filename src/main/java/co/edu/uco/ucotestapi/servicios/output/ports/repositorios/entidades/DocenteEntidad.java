package co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades;

import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.EstadoDocente;

public class DocenteEntidad {
	private UUID id;
	private String nombre;
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


	public EstadoDocente getEstado() {
		return estado;
	}

	public void setEstado(EstadoDocente estado) {
		this.estado = estado;
	}

}
