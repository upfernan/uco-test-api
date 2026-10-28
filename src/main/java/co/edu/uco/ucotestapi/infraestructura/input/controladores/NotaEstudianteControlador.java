package co.edu.uco.ucotestapi.infraestructura.input.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.consultas.ConsultarEstudiantesGrupoCasoUso;
import co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.consultas.ConsultarGruposDocenteCasoUso;
import co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.consultas.ConsultarNotasGrupoCasoUso;
import co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.registrarnotaestudiante.RegistrarNotaEstudianteCasoUso;
import co.edu.uco.ucotestapi.servicios.input.dto.request.RegistrarNotaEstudianteDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.response.EstudianteResponseDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.response.GrupoResponseDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.response.NotaResponseDTO;
import co.edu.uco.ucotestapi.transversal.respuesta.RespuestaExito;

@RestController
@RequestMapping("/api/v1")
public class NotaEstudianteControlador {
	private final RegistrarNotaEstudianteCasoUso registrarNotaEstudiante;
	private final ConsultarGruposDocenteCasoUso consultarGruposDocente;
	private final ConsultarEstudiantesGrupoCasoUso consultarEstudiantesGrupo;
	private final ConsultarNotasGrupoCasoUso consultarNotasGrupo;

	public NotaEstudianteControlador(RegistrarNotaEstudianteCasoUso registrarNotaEstudiante,
			ConsultarGruposDocenteCasoUso consultarGruposDocente,
			ConsultarEstudiantesGrupoCasoUso consultarEstudiantesGrupo,
			ConsultarNotasGrupoCasoUso consultarNotasGrupo) {
		this.registrarNotaEstudiante = registrarNotaEstudiante;
		this.consultarGruposDocente = consultarGruposDocente;
		this.consultarEstudiantesGrupo = consultarEstudiantesGrupo;
		this.consultarNotasGrupo = consultarNotasGrupo;
	}

	@PostMapping("/notas-estudiantes")
	public ResponseEntity<RespuestaExito<Void>> registrarNota(@RequestBody RegistrarNotaEstudianteDTO datos) {
		registrarNotaEstudiante.ejecutar(datos);
		return new ResponseEntity<>(RespuestaExito.crear("La nota se registró correctamente", null), HttpStatus.CREATED);
	}

	@GetMapping("/docentes/{idDocente}/grupos")
	public ResponseEntity<RespuestaExito<List<GrupoResponseDTO>>> consultarGruposDocente(@PathVariable UUID idDocente) {
		return ResponseEntity.ok(RespuestaExito.crear("Grupos del docente", consultarGruposDocente.ejecutar(idDocente)));
	}

	@GetMapping("/grupos/{idGrupo}/estudiantes")
	public ResponseEntity<RespuestaExito<List<EstudianteResponseDTO>>> consultarEstudiantesGrupo(@PathVariable UUID idGrupo) {
		return ResponseEntity.ok(RespuestaExito.crear("Estudiantes del grupo", consultarEstudiantesGrupo.ejecutar(idGrupo)));
	}

	@GetMapping("/grupos/{idGrupo}/notas")
	public ResponseEntity<RespuestaExito<List<NotaResponseDTO>>> consultarNotasGrupo(@PathVariable UUID idGrupo) {
		return ResponseEntity.ok(RespuestaExito.crear("Notas (actividades) del grupo", consultarNotasGrupo.ejecutar(idGrupo)));
	}

}