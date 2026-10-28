package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "EstudianteGrupo")
public class EstudianteGrupoEntidadJpa {
	@Id
	@Column(name = "id")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "id_estudiante")
	private EstudianteEntidadJpa estudiante;

	@ManyToOne
	@JoinColumn(name = "id_grupo")
	private GrupoEntidadJpa grupo;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public EstudianteEntidadJpa getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(EstudianteEntidadJpa estudiante) {
		this.estudiante = estudiante;
	}

	public GrupoEntidadJpa getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoEntidadJpa grupo) {
		this.grupo = grupo;
	}

}
