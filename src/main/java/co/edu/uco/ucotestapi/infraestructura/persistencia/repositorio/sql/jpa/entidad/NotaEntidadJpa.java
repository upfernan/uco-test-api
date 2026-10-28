package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad;

import java.time.Instant;
import java.util.UUID;

import co.edu.uco.ucotestapi.transversal.enumeracion.EstadoNota;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "Nota")
public class NotaEntidadJpa {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "nombre")
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "id_grupo")
	private GrupoEntidadJpa grupo;

	@Column(name = "fecha_registro_desde")
	private Instant fechaRegistroDesde;

	@Column(name = "fecha_registro_hasta")
	private Instant fechaRegistroHasta;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
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


	public GrupoEntidadJpa getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoEntidadJpa grupo) {
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
