package co.edu.uco.ucotestcatalogo;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mensajes")
public class CatalogoMensajesControlador {
	private final CatalogoMensajes catalogo;

	public CatalogoMensajesControlador(final CatalogoMensajes catalogo) {
		this.catalogo = catalogo;
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<MensajeDTO> consultar(@PathVariable final String codigo) {
		return catalogo.consultar(codigo)
				.map(contenido -> ResponseEntity.ok(new MensajeDTO(codigo, contenido)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public Map<String, String> consultarTodos() {
		return catalogo.consultarTodos();
	}

	public record MensajeDTO(String codigo, String contenido) {
	}

}
