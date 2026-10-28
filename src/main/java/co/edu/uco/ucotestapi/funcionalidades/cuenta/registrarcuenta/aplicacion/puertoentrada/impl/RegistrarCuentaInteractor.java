package co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.EjecucionSegura;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.casouso.RegistrarCuentaCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.RegistrarCuentaPuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.dto.CuentaRegistradaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.dto.RegistrarCuentaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.registrarcuenta.aplicacion.puertoentrada.impl.mapeador.RegistrarCuentaMapeadorPuertoEntrada;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegistrarCuentaInteractor implements RegistrarCuentaPuertoEntrada {
	private final RegistrarCuentaCasoUso casoUso;
	private final RegistrarCuentaMapeadorPuertoEntrada mapeador;

	public RegistrarCuentaInteractor(final RegistrarCuentaCasoUso casoUso, final RegistrarCuentaMapeadorPuertoEntrada mapeador) {
		this.casoUso = casoUso;
		this.mapeador = mapeador;
	}

	@Override
	public CuentaRegistradaDTO ejecutar(final RegistrarCuentaDTO datos) {
		return EjecucionSegura.ejecutar(() -> mapeador.aDTO(casoUso.ejecutar(mapeador.aDominio(datos))),
				CodigoMensaje.REGISTRAR_CUENTA_ERROR_INESPERADO_USUARIO, CodigoMensaje.REGISTRAR_CUENTA_ERROR_INESPERADO_TECNICO);
	}

}
