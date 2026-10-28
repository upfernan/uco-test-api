package co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.ConsultarNotasGrupoCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.ConsultarNotasGrupoPuertoEntrada;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.dto.NotaGrupoDTO;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.puertoentrada.impl.mapeador.ConsultarNotasGrupoMapeadorPuertoEntrada;

@Service
@Transactional(readOnly = true)
public class ConsultarNotasGrupoInteractor implements ConsultarNotasGrupoPuertoEntrada {
	private final ConsultarNotasGrupoCasoUso casoUso;
	private final ConsultarNotasGrupoMapeadorPuertoEntrada mapeador;

	public ConsultarNotasGrupoInteractor(final ConsultarNotasGrupoCasoUso casoUso, final ConsultarNotasGrupoMapeadorPuertoEntrada mapeador) {
		this.casoUso = casoUso;
		this.mapeador = mapeador;
	}

	@Override
	public List<NotaGrupoDTO> ejecutar(final UUID idGrupo) {
		return casoUso.ejecutar(idGrupo).stream().map(mapeador::aDTO).toList();
	}

}
