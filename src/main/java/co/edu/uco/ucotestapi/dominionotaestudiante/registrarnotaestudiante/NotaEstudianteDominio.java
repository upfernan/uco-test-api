package co.edu.uco.ucotestapi.dominionotaestudiante.registrarnotaestudiante;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaEstudianteRepositorio;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilNumero;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

public class NotaEstudianteDominio {
	private static final int VALOR_MINIMO = 0;
	private static final int VALOR_MAXIMO = 500;
	private static final int MAXIMO_DECIMALES = 2;

	private UUID idEstudiante;
	private UUID idNota;
	private int valor;
	private NotaEstudianteRepositorio notaEstudianteRepositorio;

	public NotaEstudianteDominio(UUID idEstudiante, UUID idNota, int valor, NotaEstudianteRepositorio notaEstudianteRepositorio) {
		this.idEstudiante = UtilUUID.obtenerValorDefecto(idEstudiante);
		this.idNota = UtilUUID.obtenerValorDefecto(idNota);
		this.valor = valor;
		this.notaEstudianteRepositorio = Objects.requireNonNull(notaEstudianteRepositorio, "El repositorio de notas por estudiante es obligatorio");
	}

	// Regla 10: convierte la nota real (ej. 3.5) al valor escalado que se guarda (ej. 350)
	public static int escalarValor(BigDecimal valorReal) {
		if (UtilObjeto.esNulo(valorReal)) {
			throw new ReglaNegocioException(CodigoMensaje.VALOR_NOTA_NULO);
		}
		if (valorReal.stripTrailingZeros().scale() > MAXIMO_DECIMALES) {
			throw new ReglaNegocioException(CodigoMensaje.VALOR_NOTA_MAXIMO_DOS_DECIMALES);
		}
		return valorReal.movePointRight(MAXIMO_DECIMALES).intValue();
	}

	// Regla 6
	public void validarNotaNoRegistrada() {
		if (UtilUUID.esValorDefecto(idEstudiante) || UtilUUID.esValorDefecto(idNota)) {
			throw new ReglaNegocioException(CodigoMensaje.NOTA_ESTUDIANTE_IDS_NULOS);
		}
		if (notaEstudianteRepositorio.existePorEstudianteYNota(idEstudiante, idNota)) {
			throw new ReglaNegocioException(CodigoMensaje.NOTA_YA_REGISTRADA, idEstudiante, idNota);
		}
	}

	// Regla 9: el valor esta escalado, 0 a 500 equivale a 0.00 a 5.00
	public void validarValorEnRango() {
		if (!UtilNumero.estaEntre(valor, VALOR_MINIMO, VALOR_MAXIMO)) {
			throw new ReglaNegocioException(CodigoMensaje.VALOR_NOTA_FUERA_DE_RANGO);
		}
	}

}
