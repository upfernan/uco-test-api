package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.seguridad;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.CatalogoMensajesPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.respuesta.Respuesta;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.json.JsonMapper;

/**
 * Los errores 401 y 403 ocurren antes de llegar al controlador, asi que el GlobalExceptionHandler no los ve.
 * Aqui se responden con el mismo formato Respuesta y con mensajes del catalogo.
 */
@Component
public class RespuestaErrorSeguridad implements AuthenticationEntryPoint, AccessDeniedHandler {
	private final CatalogoMensajesPuertoSalida catalogoMensajes;
	private final JsonMapper jsonMapper;

	public RespuestaErrorSeguridad(final CatalogoMensajesPuertoSalida catalogoMensajes, final JsonMapper jsonMapper) {
		this.catalogoMensajes = catalogoMensajes;
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void commence(final HttpServletRequest peticion, final HttpServletResponse respuesta,
			final AuthenticationException excepcion) throws IOException {
		escribir(respuesta, HttpStatus.UNAUTHORIZED, CodigoMensaje.NO_AUTENTICADO);
	}

	@Override
	public void handle(final HttpServletRequest peticion, final HttpServletResponse respuesta,
			final AccessDeniedException excepcion) throws IOException {
		escribir(respuesta, HttpStatus.FORBIDDEN, CodigoMensaje.ACCESO_DENEGADO);
	}

	private void escribir(final HttpServletResponse respuesta, final HttpStatus estado, final CodigoMensaje codigo)
			throws IOException {
		var mensaje = catalogoMensajes.consultarMensaje(codigo).contenido();
		respuesta.setStatus(estado.value());
		respuesta.setContentType(MediaType.APPLICATION_JSON_VALUE);
		respuesta.setCharacterEncoding(StandardCharsets.UTF_8.name());
		respuesta.getWriter().write(jsonMapper.writeValueAsString(Respuesta.crearFallida(mensaje)));
	}

}