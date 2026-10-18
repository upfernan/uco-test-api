package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaEstudianteRepositorio;

public class NotaEstudianteDominio {
	private static final int VALOR_MINIMO = 0;
	private static final int VALOR_MAXIMO = 500;
	private static final int MAXIMO_DECIMALES = 2;

	private UUID idEstudiante;
	private UUID idNota;
	private int valor;
	private NotaEstudianteRepositorio notaEstudianteRepositorio;

	public NotaEstudianteDominio(UUID idEstudiante, UUID idNota, int valor, NotaEstudianteRepositorio notaEstudianteRepositorio) {
		this.idEstudiante = idEstudiante;
		this.idNota = idNota;
		this.valor = valor;
		this.notaEstudianteRepositorio = Objects.requireNonNull(notaEstudianteRepositorio, "El repositorio de notas por estudiante es obligatorio");
	}

	// Regla 10: convierte la nota real (ej. 3.5) al valor escalado que se guarda (ej. 350)
	public static int escalarValor(BigDecimal valorReal) {
		if (valorReal == null) {
			throw new IllegalArgumentException("El valor de la nota no puede ser nulo");
		}
		if (valorReal.stripTrailingZeros().scale() > MAXIMO_DECIMALES) {
			throw new IllegalArgumentException("El valor de la nota debe tener maximo dos decimales");
		}
		return valorReal.movePointRight(MAXIMO_DECIMALES).intValue();
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

	// Regla 9: el valor esta escalado, 0 a 500 equivale a 0.00 a 5.00
	public void validarValorEnRango() {
		if (valor < VALOR_MINIMO || valor > VALOR_MAXIMO) {
			throw new IllegalArgumentException("El valor de la nota debe estar entre 0 y 5");
		}
	}

}
