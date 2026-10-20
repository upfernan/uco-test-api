package co.edu.uco.ucotestapi.transversal.excepcion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import co.edu.uco.ucotestapi.dominio.excepciones.ReglaNegocioException;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CatalogoMensajesServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.respuesta.RespuestaError;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private final CatalogoMensajesServicio catalogoMensajes;

	public GlobalExceptionHandler(final CatalogoMensajesServicio catalogoMensajes) {
		this.catalogoMensajes = catalogoMensajes;
	}

	@ExceptionHandler(UcoTestApiExcepcion.class)
	public ResponseEntity<RespuestaError> gestionar(final UcoTestApiExcepcion excepcion) {
		LOGGER.error("Error controlado: {}", excepcion.getMensajeTecnico(), excepcion.getExcepcionRaiz());
		return new ResponseEntity<>(RespuestaError.crear(excepcion.getMensajeUsuario()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ReglaNegocioException.class)
	public ResponseEntity<RespuestaError> gestionar(final ReglaNegocioException excepcion) {
		var mensajeUsuario = mensaje(excepcion.getCodigo(), excepcion.getParametros());
		LOGGER.error(mensaje(CodigoMensaje.REGLA_NEGOCIO_INCUMPLIDA_TECNICO, excepcion.getMessage()), excepcion);
		return new ResponseEntity<>(RespuestaError.crear(mensajeUsuario), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
	public ResponseEntity<RespuestaError> gestionarDatosInvalidos(final Exception excepcion) {
		LOGGER.error(mensaje(CodigoMensaje.DATOS_PETICION_INVALIDOS_TECNICO), excepcion);
		return new ResponseEntity<>(RespuestaError.crear(mensaje(CodigoMensaje.DATOS_PETICION_INVALIDOS_USUARIO)),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RespuestaError> gestionar(final Exception excepcion) {
		LOGGER.error(mensaje(CodigoMensaje.ERROR_INESPERADO_TECNICO), excepcion);
		return new ResponseEntity<>(RespuestaError.crear(mensaje(CodigoMensaje.ERROR_INESPERADO_USUARIO)),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private String mensaje(final CodigoMensaje codigo, final Object... parametros) {
		return catalogoMensajes.consultarMensaje(codigo, parametros).contenido();
	}

}