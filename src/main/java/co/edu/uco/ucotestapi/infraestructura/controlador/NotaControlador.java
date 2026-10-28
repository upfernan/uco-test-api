package co.edu.uco.ucotestapi.infraestructura.controlador;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.ConsultarNotasGrupoPuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.dto.NotaGrupoDTO;
import co.edu.uco.ucotestapi.transversal.respuesta.RespuestaExito;

@RestController
@RequestMapping("/api/v1")
public class NotaControlador {
	private final ConsultarNotasGrupoPuertoEntrada consultarNotasGrupo;

	public NotaControlador(final ConsultarNotasGrupoPuertoEntrada consultarNotasGrupo) {
		this.consultarNotasGrupo = consultarNotasGrupo;
	}

	@GetMapping("/grupos/{idGrupo}/notas")
	public ResponseEntity<RespuestaExito<List<NotaGrupoDTO>>> consultarNotasGrupo(@PathVariable final UUID idGrupo) {
		return ResponseEntity.ok(RespuestaExito.crear("Notas (actividades) del grupo", consultarNotasGrupo.ejecutar(idGrupo)));
	}

}