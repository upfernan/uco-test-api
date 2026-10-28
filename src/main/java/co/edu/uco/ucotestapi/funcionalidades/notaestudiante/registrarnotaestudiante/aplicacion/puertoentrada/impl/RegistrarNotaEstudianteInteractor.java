package co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.aplicacion.puertoentrada.EjecucionSegura;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.RegistrarNotaEstudianteCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.RegistrarNotaEstudiantePuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.dto.RegistrarNotaEstudianteDTO;
import co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.puertoentrada.impl.mapeador.RegistrarNotaEstudianteMapeadorPuertoEntrada;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegistrarNotaEstudianteInteractor implements RegistrarNotaEstudiantePuertoEntrada {
	private final RegistrarNotaEstudianteCasoUso casoUso;
	private final RegistrarNotaEstudianteMapeadorPuertoEntrada mapeador;

	public RegistrarNotaEstudianteInteractor(final RegistrarNotaEstudianteCasoUso casoUso,
			final RegistrarNotaEstudianteMapeadorPuertoEntrada mapeador) {
		this.casoUso = casoUso;
		this.mapeador = mapeador;
	}

	@Override
	public Void ejecutar(final RegistrarNotaEstudianteDTO datos) {
		return EjecucionSegura.ejecutar(() -> casoUso.ejecutar(mapeador.aDominio(datos)),
				CodigoMensaje.REGISTRAR_NOTA_ERROR_INESPERADO_USUARIO, CodigoMensaje.REGISTRAR_NOTA_ERROR_INESPERADO_TECNICO);
	}

}
