package co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertoentrada.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.EjecucionSegura;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.casouso.SolicitarRecuperacionClaveCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertoentrada.SolicitarRecuperacionClavePuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertoentrada.dto.SolicitarRecuperacionClaveDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertoentrada.impl.mapeador.SolicitarRecuperacionClaveMapeadorPuertoEntrada;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;

@Service
@Transactional(rollbackFor = Exception.class)
public class SolicitarRecuperacionClaveInteractor implements SolicitarRecuperacionClavePuertoEntrada {
	private final SolicitarRecuperacionClaveCasoUso casoUso;
	private final SolicitarRecuperacionClaveMapeadorPuertoEntrada mapeador;

	public SolicitarRecuperacionClaveInteractor(final SolicitarRecuperacionClaveCasoUso casoUso,
			final SolicitarRecuperacionClaveMapeadorPuertoEntrada mapeador) {
		this.casoUso = casoUso;
		this.mapeador = mapeador;
	}

	@Override
	public Void ejecutar(final SolicitarRecuperacionClaveDTO datos) {
		return EjecucionSegura.ejecutar(() -> casoUso.ejecutar(mapeador.aDominio(datos)),
				CodigoMensaje.SOLICITAR_RECUPERACION_ERROR_INESPERADO_USUARIO,
				CodigoMensaje.SOLICITAR_RECUPERACION_ERROR_INESPERADO_TECNICO);
	}

}
