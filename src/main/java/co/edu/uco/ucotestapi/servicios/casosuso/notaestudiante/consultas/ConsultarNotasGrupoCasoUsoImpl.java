package co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.consultas;

import co.edu.uco.ucotestapi.servicios.input.ports.notaestudiante.ConsultarNotasGrupoCasoUso;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.NotaRepositorio;
import co.edu.uco.ucotestapi.servicios.input.dto.response.NotaResponseDTO;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class ConsultarNotasGrupoCasoUsoImpl implements ConsultarNotasGrupoCasoUso {
	private final NotaRepositorio notaRepositorio;

	public ConsultarNotasGrupoCasoUsoImpl(NotaRepositorio notaRepositorio) {
		this.notaRepositorio = notaRepositorio;
	}

	@Override
	@Transactional(readOnly = true)
	public List<NotaResponseDTO> ejecutar(UUID idGrupo) {
		return notaRepositorio.consultarPorGrupo(UtilUUID.obtenerValorDefecto(idGrupo)).stream()
				.map(nota -> new NotaResponseDTO(nota.getId(), nota.getNombre(), nota.getEstado().name(),
						nota.getFechaRegistroDesde(), nota.getFechaRegistroHasta()))
				.toList();
	}

}