package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "EstudianteGrupo")
public class EstudianteGrupoEntidadJPA {
	@Id
	@Column(name = "id")
	private UUID id;
	private EstudianteEntidadJPA estudiante;
	private GrupoEntidadJPA grupo;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public EstudianteEntidadJPA getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(EstudianteEntidadJPA estudiante) {
		this.estudiante = estudiante;
	}

	public GrupoEntidadJPA getGrupo() {
		return grupo;
	}

	public void setGrupo(GrupoEntidadJPA grupo) {
		this.grupo = grupo;
	}

}
