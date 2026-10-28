package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad;

import java.time.LocalDate;
import java.util.UUID;

import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoGrupo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "Grupo")
public class GrupoEntidadJpa {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "nombre")
	private String nombre;
	
	
	@ManyToOne
	@JoinColumn(name = "id_docente")
	private DocenteEntidadJpa docente;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	private EstadoGrupo estado;

	@Column(name = "fecha_inicio")
	private LocalDate fechaInicio;

	@Column(name = "fecha_fin")
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


	public DocenteEntidadJpa getDocente() {
		return docente;
	}

	public void setDocente(DocenteEntidadJpa docente) {
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
