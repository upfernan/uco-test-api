package co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.ConsultarGruposDocenteCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.ConsultarGruposDocentePuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.dto.GrupoDocenteDTO;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.puertoentrada.impl.mapeador.ConsultarGruposDocenteMapeadorPuertoEntrada;

@Service
@Transactional(readOnly = true)
public class ConsultarGruposDocenteInteractor implements ConsultarGruposDocentePuertoEntrada {
	private final ConsultarGruposDocenteCasoUso casoUso;
	private final ConsultarGruposDocenteMapeadorPuertoEntrada mapeador;

	public ConsultarGruposDocenteInteractor(final ConsultarGruposDocenteCasoUso casoUso, final ConsultarGruposDocenteMapeadorPuertoEntrada mapeador) {
		this.casoUso = casoUso;
		this.mapeador = mapeador;
	}

	@Override
	public List<GrupoDocenteDTO> ejecutar(final UUID idDocente) {
		return casoUso.ejecutar(idDocente).stream().map(mapeador::aDTO).toList();
	}

}
