package co.edu.uco.ucotestapi.infraestructura.controlador;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.ConsultarEstudiantesGrupoPuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.dto.EstudianteGrupoDTO;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.ConsultarGruposDocentePuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.dto.GrupoDocenteDTO;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.respuesta.Respuesta;

@RestController
@RequestMapping("/api/v1")
public class GrupoControlador {
	private final ConsultarGruposDocentePuertoEntrada consultarGruposDocente;
	private final ConsultarEstudiantesGrupoPuertoEntrada consultarEstudiantesGrupo;

	public GrupoControlador(final ConsultarGruposDocentePuertoEntrada consultarGruposDocente,
			final ConsultarEstudiantesGrupoPuertoEntrada consultarEstudiantesGrupo) {
		this.consultarGruposDocente = consultarGruposDocente;
		this.consultarEstudiantesGrupo = consultarEstudiantesGrupo;
	}

	@GetMapping("/docentes/{idDocente}/grupos")
	public ResponseEntity<Respuesta<GrupoDocenteDTO>> consultarGruposDocente(@PathVariable final UUID idDocente) {
		return ResponseEntity.ok(Respuesta.crearExitosa(consultarGruposDocente.ejecutar(idDocente))
				.agregarMensaje(CatalogoMensajesProveedor.contenido(CodigoMensaje.GRUPOS_DOCENTE_CONSULTADOS)));
	}

	@GetMapping("/grupos/{idGrupo}/estudiantes")
	public ResponseEntity<Respuesta<EstudianteGrupoDTO>> consultarEstudiantesGrupo(@PathVariable final UUID idGrupo) {
		return ResponseEntity.ok(Respuesta.crearExitosa(consultarEstudiantesGrupo.ejecutar(idGrupo))
				.agregarMensaje(CatalogoMensajesProveedor.contenido(CodigoMensaje.ESTUDIANTES_GRUPO_CONSULTADOS)));
	}

}