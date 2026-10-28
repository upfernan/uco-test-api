package co.edu.uco.ucotestapi.infraestructura.controlador;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.RegistrarNotaEstudiantePuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.dto.RegistrarNotaEstudianteDTO;
import co.edu.uco.ucotestapi.infraestructura.controlador.dto.RegistrarNotaSolicitudDTO;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.seguridad.ClaimsToken;
import co.edu.uco.ucotestapi.transversal.respuesta.RespuestaExito;

@RestController
@RequestMapping("/api/v1/notas-estudiantes")
public class NotaEstudianteControlador {
	private final RegistrarNotaEstudiantePuertoEntrada registrarNotaEstudiante;

	public NotaEstudianteControlador(final RegistrarNotaEstudiantePuertoEntrada registrarNotaEstudiante) {
		this.registrarNotaEstudiante = registrarNotaEstudiante;
	}

	@PostMapping
	public ResponseEntity<RespuestaExito<Void>> registrarNota(@AuthenticationPrincipal final Jwt sesion,
			@RequestBody final RegistrarNotaSolicitudDTO datos) {
		var idDocente = UUID.fromString(sesion.getClaimAsString(ClaimsToken.ID_PERSONA));
		registrarNotaEstudiante.ejecutar(new RegistrarNotaEstudianteDTO(idDocente, datos.idGrupo(), datos.idEstudiante(),
				datos.idNota(), datos.valor()));
		return new ResponseEntity<>(RespuestaExito.crear("La nota se registró correctamente", null), HttpStatus.CREATED);
	}

}