package co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades;

import java.time.LocalDate;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.EstadoGrupo;

public class GrupoEntidad {
	private UUID id;
	private String nombre;
	private DocenteEntidad docente;
	private EstadoGrupo estado;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;

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


	public DocenteEntidad getDocente() {
		return docente;
	}

	public void setDocente(DocenteEntidad docente) {
		this.docente = docente;
	}

	public EstadoGrupo getEstado() {
		return estado;
	}

	public void setEstado(EstadoGrupo estado) {
		this.estado = estado;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

}
