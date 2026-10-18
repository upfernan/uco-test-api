package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "NotaEstudiante")
public class NotaEstudianteEntidadJPA {
	@Id
	@Column(name = "id")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "id_estudiante_grupo")
	private EstudianteGrupoEntidadJPA estudianteGrupo;

	@ManyToOne
	@JoinColumn(name = "id_nota")
	private NotaEntidadJPA nota;

	@Column(name = "valor")
	private int valor;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public EstudianteGrupoEntidadJPA getEstudianteGrupo() {
		return estudianteGrupo;
	}

	public void setEstudianteGrupo(EstudianteGrupoEntidadJPA estudianteGrupo) {
		this.estudianteGrupo = estudianteGrupo;
	}

	public NotaEntidadJPA getNota() {
		return nota;
	}

	public void setNota(NotaEntidadJPA nota) {
		this.nota = nota;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

}
