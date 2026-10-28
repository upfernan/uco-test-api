package co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.puertoentrada.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.EjecucionSegura;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.casouso.RestablecerClaveCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.puertoentrada.RestablecerClavePuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.puertoentrada.dto.RestablecerClaveDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.puertoentrada.impl.mapeador.RestablecerClaveMapeadorPuertoEntrada;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

/**
 * Un codigo equivocado responde error pero debe dejar guardado el intento fallido,
 * por eso la transaccion no se deshace con las reglas de negocio incumplidas.
 */
@Service
@Transactional(rollbackFor = Exception.class, noRollbackFor = UcoTestApiExcepcion.class)
public class RestablecerClaveInteractor implements RestablecerClavePuertoEntrada {
	private final RestablecerClaveCasoUso casoUso;
	private final RestablecerClaveMapeadorPuertoEntrada mapeador;

	public RestablecerClaveInteractor(final RestablecerClaveCasoUso casoUso, final RestablecerClaveMapeadorPuertoEntrada mapeador) {
		this.casoUso = casoUso;
		this.mapeador = mapeador;
	}

	@Override
	public Void ejecutar(final RestablecerClaveDTO datos) {
		return EjecucionSegura.ejecutar(() -> casoUso.ejecutar(mapeador.aDominio(datos)),
				CodigoMensaje.RESTABLECER_CLAVE_ERROR_INESPERADO_USUARIO, CodigoMensaje.RESTABLECER_CLAVE_ERROR_INESPERADO_TECNICO);
	}

}
