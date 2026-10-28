package co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.dominio;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Datos de entrada del caso de uso. Las reglas de negocio las validan los objetos de dominio
 * (GrupoDominio, DocenteDominio, EstudianteDominio, EstudianteGrupoDominio, NotaDominio y NotaEstudianteDominio).
 */
public class RegistrarNotaEstudianteDominio {
	private final UUID idDocente;
	private final UUID idGrupo;
	private final UUID idEstudiante;
	private final UUID idNota;
	private final BigDecimal valor;

	public RegistrarNotaEstudianteDominio(final UUID idDocente, final UUID idGrupo, final UUID idEstudiante,
			final UUID idNota, final BigDecimal valor) {
		this.idDocente = idDocente;
		this.idGrupo = idGrupo;
		this.idEstudiante = idEstudiante;
		this.idNota = idNota;
		this.valor = valor;
	}

	public UUID getIdDocente() {
		return idDocente;
	}

	public UUID getIdGrupo() {
		return idGrupo;
	}

	public UUID getIdEstudiante() {
		return idEstudiante;
	}

	public UUID getIdNota() {
		return idNota;
	}

	public BigDecimal getValor() {
		return valor;
	}

}