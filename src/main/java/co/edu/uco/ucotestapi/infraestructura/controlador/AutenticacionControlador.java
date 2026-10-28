package co.edu.uco.ucotestapi.infraestructura.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.dto.IniciarSesionDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.dto.RegistrarCuentaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.dto.CuentaRegistradaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.dto.SesionIniciadaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.IniciarSesionPuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.RegistrarCuentaPuertoEntrada;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CatalogoMensajesProveedor;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.respuesta.Respuesta;

@RestController
@RequestMapping("/api/v1/auth")
public class AutenticacionControlador {
	private final RegistrarCuentaPuertoEntrada registrarCuenta;
	private final IniciarSesionPuertoEntrada iniciarSesion;

	public AutenticacionControlador(RegistrarCuentaPuertoEntrada registrarCuenta, IniciarSesionPuertoEntrada iniciarSesion) {
		this.registrarCuenta = registrarCuenta;
		this.iniciarSesion = iniciarSesion;
	}

	@PostMapping("/registro")
	public ResponseEntity<Respuesta<CuentaRegistradaDTO>> registrar(@RequestBody RegistrarCuentaDTO datos) {
		var respuesta = Respuesta.crearExitosa(registrarCuenta.ejecutar(datos))
				.agregarMensaje(CatalogoMensajesProveedor.contenido(CodigoMensaje.CUENTA_REGISTRADA));
		return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<Respuesta<SesionIniciadaDTO>> iniciarSesion(@RequestBody IniciarSesionDTO datos) {
		return ResponseEntity.ok(Respuesta.crearExitosa(iniciarSesion.ejecutar(datos))
				.agregarMensaje(CatalogoMensajesProveedor.contenido(CodigoMensaje.SESION_INICIADA)));
	}

}
