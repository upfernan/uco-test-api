package co.edu.uco.ucotestapi.infraestructura.controlador;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.ConsultarNotasGrupoPuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.dto.NotaGrupoDTO;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.respuesta.Respuesta;

@RestController
@RequestMapping("/api/v1")
public class NotaControlador {
	private final ConsultarNotasGrupoPuertoEntrada consultarNotasGrupo;

	public NotaControlador(final ConsultarNotasGrupoPuertoEntrada consultarNotasGrupo) {
		this.consultarNotasGrupo = consultarNotasGrupo;
	}

	@GetMapping("/grupos/{idGrupo}/notas")
	public ResponseEntity<Respuesta<NotaGrupoDTO>> consultarNotasGrupo(@PathVariable final UUID idGrupo) {
		return ResponseEntity.ok(Respuesta.crearExitosa(consultarNotasGrupo.ejecutar(idGrupo))
				.agregarMensaje(CatalogoMensajesProveedor.contenido(CodigoMensaje.NOTAS_GRUPO_CONSULTADAS)));
	}

}