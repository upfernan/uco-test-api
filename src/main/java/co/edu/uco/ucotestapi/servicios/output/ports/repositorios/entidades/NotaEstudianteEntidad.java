package co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades;

import java.math.BigDecimal;
import java.util.UUID;

public class NotaEstudianteEntidad {
	private UUID id;
	private EstudianteGrupoEntidad estudianteGrupo;
	private NotaEntidad nota;
	private BigDecimal valor;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public EstudianteGrupoEntidad getEstudianteGrupo() {
		return estudianteGrupo;
	}

	public void setEstudianteGrupo(EstudianteGrupoEntidad estudianteGrupo) {
		this.estudianteGrupo = estudianteGrupo;
	}

	public NotaEntidad getNota() {
		return nota;
	}

	public void setNota(NotaEntidad nota) {
		this.nota = nota;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
