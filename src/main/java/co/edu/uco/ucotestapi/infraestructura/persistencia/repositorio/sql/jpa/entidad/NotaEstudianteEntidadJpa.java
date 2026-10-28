package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "NotaEstudiante")
public class NotaEstudianteEntidadJpa {
	@Id
	@Column(name = "id")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "id_estudiante_grupo")
	private EstudianteGrupoEntidadJpa estudianteGrupo;

	@ManyToOne
	@JoinColumn(name = "id_nota")
	private NotaEntidadJpa nota;

	@Column(name = "valor")
	private int valor;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public EstudianteGrupoEntidadJpa getEstudianteGrupo() {
		return estudianteGrupo;
	}

	public void setEstudianteGrupo(EstudianteGrupoEntidadJpa estudianteGrupo) {
		this.estudianteGrupo = estudianteGrupo;
	}

	public NotaEntidadJpa getNota() {
		return nota;
	}

	public void setNota(NotaEntidadJpa nota) {
		this.nota = nota;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

}
