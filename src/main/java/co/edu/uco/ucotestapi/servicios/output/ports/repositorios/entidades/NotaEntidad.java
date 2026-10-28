package co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades;

import java.time.Instant;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.enums.EstadoNota;

public class NotaEntidad {
	private UUID id;
	private String nombre;
	private GrupoEntidad grupo;
	private Instant fechaRegistroDesde;
	private Instant fechaRegistroHasta;
	private EstadoNota estado;

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


	public GrupoEntidad getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoEntidad grupo) {
		this.grupo = grupo;
	}

	public Instant getFechaRegistroDesde() {
		return fechaRegistroDesde;
	}

	public void setFechaRegistroDesde(Instant fechaRegistroDesde) {
		this.fechaRegistroDesde = fechaRegistroDesde;
	}

	public Instant getFechaRegistroHasta() {
		return fechaRegistroHasta;
	}

	public void setFechaRegistroHasta(Instant fechaRegistroHasta) {
		this.fechaRegistroHasta = fechaRegistroHasta;
	}

	public EstadoNota getEstado() {
		return estado;
	}

	public void setEstado(EstadoNota estado) {
		this.estado = estado;
	}

}
