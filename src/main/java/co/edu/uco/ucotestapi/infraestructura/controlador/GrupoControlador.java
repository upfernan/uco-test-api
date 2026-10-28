package co.edu.uco.ucotestapi.infraestructura.controlador;

import java.util.List;
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
import co.edu.uco.ucotestapi.transversal.respuesta.RespuestaExito;

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
	public ResponseEntity<RespuestaExito<List<GrupoDocenteDTO>>> consultarGruposDocente(@PathVariable final UUID idDocente) {
		return ResponseEntity.ok(RespuestaExito.crear("Grupos del docente", consultarGruposDocente.ejecutar(idDocente)));
	}

	@GetMapping("/grupos/{idGrupo}/estudiantes")
	public ResponseEntity<RespuestaExito<List<EstudianteGrupoDTO>>> consultarEstudiantesGrupo(@PathVariable final UUID idGrupo) {
		return ResponseEntity.ok(RespuestaExito.crear("Estudiantes del grupo", consultarEstudiantesGrupo.ejecutar(idGrupo)));
	}

}