package co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.casouso.ConsultarEstudiantesGrupoCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.ConsultarEstudiantesGrupoPuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.dto.EstudianteGrupoDTO;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.puertoentrada.impl.mapeador.ConsultarEstudiantesGrupoMapeadorPuertoEntrada;

@Service
@Transactional(readOnly = true)
public class ConsultarEstudiantesGrupoInteractor implements ConsultarEstudiantesGrupoPuertoEntrada {
	private final ConsultarEstudiantesGrupoCasoUso casoUso;
	private final ConsultarEstudiantesGrupoMapeadorPuertoEntrada mapeador;

	public ConsultarEstudiantesGrupoInteractor(final ConsultarEstudiantesGrupoCasoUso casoUso, final ConsultarEstudiantesGrupoMapeadorPuertoEntrada mapeador) {
		this.casoUso = casoUso;
		this.mapeador = mapeador;
	}

	@Override
	public List<EstudianteGrupoDTO> ejecutar(final UUID idGrupo) {
		return casoUso.ejecutar(idGrupo).stream().map(mapeador::aDTO).toList();
	}

}
