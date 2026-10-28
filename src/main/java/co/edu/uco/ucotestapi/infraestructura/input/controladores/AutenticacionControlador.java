package co.edu.uco.ucotestapi.infraestructura.input.controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucotestapi.servicios.input.dto.request.IniciarSesionDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.request.RegistrarCuentaDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.response.CuentaResponseDTO;
import co.edu.uco.ucotestapi.servicios.input.dto.response.SesionResponseDTO;
import co.edu.uco.ucotestapi.servicios.input.ports.usuario.IniciarSesionCasoUso;
import co.edu.uco.ucotestapi.servicios.input.ports.usuario.RegistrarCuentaCasoUso;
import co.edu.uco.ucotestapi.transversal.respuesta.RespuestaExito;

@RestController
@RequestMapping("/api/v1/auth")
public class AutenticacionControlador {
	private final RegistrarCuentaCasoUso registrarCuenta;
	private final IniciarSesionCasoUso iniciarSesion;

	public AutenticacionControlador(RegistrarCuentaCasoUso registrarCuenta, IniciarSesionCasoUso iniciarSesion) {
		this.registrarCuenta = registrarCuenta;
		this.iniciarSesion = iniciarSesion;
	}

	@PostMapping("/registro")
	public ResponseEntity<RespuestaExito<CuentaResponseDTO>> registrar(@RequestBody RegistrarCuentaDTO datos) {
		return new ResponseEntity<>(RespuestaExito.crear("La cuenta se registró correctamente", registrarCuenta.ejecutar(datos)),
				HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<RespuestaExito<SesionResponseDTO>> iniciarSesion(@RequestBody IniciarSesionDTO datos) {
		return ResponseEntity.ok(RespuestaExito.crear("Sesión iniciada", iniciarSesion.ejecutar(datos)));
	}

}
