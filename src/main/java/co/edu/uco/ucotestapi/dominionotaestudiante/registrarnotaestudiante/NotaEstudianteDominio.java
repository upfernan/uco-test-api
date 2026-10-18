package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaEstudianteRepositorio;

public class NotaEstudianteDominio {
	private static final BigDecimal VALOR_MINIMO = BigDecimal.ZERO;
	private static final BigDecimal VALOR_MAXIMO = new BigDecimal("5");
	private static final int MAXIMO_DECIMALES = 2;

	private UUID idEstudiante;
	private UUID idNota;
	private BigDecimal valor;
	private NotaEstudianteRepositorio notaEstudianteRepositorio;

	public NotaEstudianteDominio(UUID idEstudiante, UUID idNota, BigDecimal valor, NotaEstudianteRepositorio notaEstudianteRepositorio) {
		this.idEstudiante = idEstudiante;
		this.idNota = idNota;
		this.valor = valor;
		this.notaEstudianteRepositorio = Objects.requireNonNull(notaEstudianteRepositorio, "El repositorio de notas por estudiante es obligatorio");
	}

	// Regla 6
	public void validarNotaNoRegistrada() {
		if (idEstudiante == null || idNota == null) {
			throw new IllegalArgumentException("El ID del estudiante y el ID de la nota no pueden ser nulos");
		}
		if (notaEstudianteRepositorio.existePorEstudianteYNota(idEstudiante, idNota)) {
			throw new IllegalArgumentException("El estudiante con ID " + idEstudiante + " ya tiene registrada la nota " + idNota);
		}
	}

	// Regla 9
	public void validarValorEnRango() {
		if (valor == null) {
			throw new IllegalArgumentException("El valor de la nota no puede ser nulo");
		}
		if (valor.compareTo(VALOR_MINIMO) < 0 || valor.compareTo(VALOR_MAXIMO) > 0) {
			throw new IllegalArgumentException("El valor de la nota debe estar entre 0 y 5");
		}
	}

	// Regla 10: se valida con el escalado (scale) del BigDecimal
	public void validarMaximoDosDecimales() {
		if (valor == null) {
			throw new IllegalArgumentException("El valor de la nota no puede ser nulo");
		}
		if (valor.stripTrailingZeros().scale() > MAXIMO_DECIMALES) {
			throw new IllegalArgumentException("El valor de la nota debe tener maximo dos decimales");
		}
	}

}