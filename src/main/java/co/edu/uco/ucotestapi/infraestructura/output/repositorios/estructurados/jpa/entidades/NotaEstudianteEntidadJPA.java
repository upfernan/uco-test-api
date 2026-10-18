package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "NotaEstudiante")
public class NotaEstudianteEntidadJPA {
	@Id
	@Column(name = "id")
	private UUID id;
	private EstudianteGrupoEntidadJPA estudianteGrupo;
	private NotaEntidadJPA nota;

	@Column(name = "valor", precision = 3, scale = 2)
	private BigDecimal valor;

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
