package co.edu.uco.ucotestapi.dominio.excepciones;

import java.util.Arrays;

import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilObjeto;

public class ReglaNegocioException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final CodigoMensaje codigo;
	private final transient Object[] parametros;

	public ReglaNegocioException(CodigoMensaje codigo, Object... parametros) {
		super(codigo.name() + " " + Arrays.toString(parametros));
		this.codigo = codigo;
		this.parametros = UtilObjeto.obtenerValorDefecto(parametros, new Object[0]);
	}

	public CodigoMensaje getCodigo() {
		return codigo;
	}

	public Object[] getParametros() {
		return parametros.clone();
	}

}