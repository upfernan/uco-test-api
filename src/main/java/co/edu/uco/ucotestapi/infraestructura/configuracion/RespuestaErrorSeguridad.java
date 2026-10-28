package co.edu.uco.ucotestapi.infraestructura.configuracion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CatalogoMensajesServicio;
import co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.messagecatalog.CodigoMensaje;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Los errores 401 y 403 ocurren antes de llegar al controlador, asi que el GlobalExceptionHandler no los ve.
 * Aqui se responden con el mismo formato que RespuestaError y con mensajes del catalogo.
 */
@Component
public class RespuestaErrorSeguridad implements AuthenticationEntryPoint, AccessDeniedHandler {
	private final CatalogoMensajesServicio catalogoMensajes;

	public RespuestaErrorSeguridad(final CatalogoMensajesServicio catalogoMensajes) {
		this.catalogoMensajes = catalogoMensajes;
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
		respuesta.getWriter().write("{\"mensaje\":\"" + escaparJson(mensaje) + "\",\"fecha\":\"" + LocalDateTime.now() + "\"}");
	}

	private static String escaparJson(final String texto) {
		return texto.replace("\\", "\\\\").replace("\"", "\\\"");
	}

}