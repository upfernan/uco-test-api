package co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.EjecucionSegura;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.casouso.IniciarSesionCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.IniciarSesionPuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.dto.IniciarSesionDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.dto.SesionIniciadaDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.iniciarsesion.aplicacion.puertoentrada.impl.mapeador.IniciarSesionMapeadorPuertoEntrada;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;

@Service
@Transactional(readOnly = true)
public class IniciarSesionInteractor implements IniciarSesionPuertoEntrada {
	private final IniciarSesionCasoUso casoUso;
	private final IniciarSesionMapeadorPuertoEntrada mapeador;

	public IniciarSesionInteractor(final IniciarSesionCasoUso casoUso, final IniciarSesionMapeadorPuertoEntrada mapeador) {
		this.casoUso = casoUso;
		this.mapeador = mapeador;
	}

	@Override
	public SesionIniciadaDTO ejecutar(final IniciarSesionDTO datos) {
		return EjecucionSegura.ejecutar(() -> mapeador.aDTO(casoUso.ejecutar(mapeador.aDominio(datos))),
				CodigoMensaje.INICIAR_SESION_ERROR_INESPERADO_USUARIO, CodigoMensaje.INICIAR_SESION_ERROR_INESPERADO_TECNICO);
	}

}
