package co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.consultas;

import co.edu.uco.ucotestapi.servicios.input.ports.notaestudiante.ConsultarGruposDocenteCasoUso;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoRepositorio;
import co.edu.uco.ucotestapi.servicios.input.dto.response.GrupoResponseDTO;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class ConsultarGruposDocenteCasoUsoImpl implements ConsultarGruposDocenteCasoUso {
	private final GrupoRepositorio grupoRepositorio;

	public ConsultarGruposDocenteCasoUsoImpl(GrupoRepositorio grupoRepositorio) {
		this.grupoRepositorio = grupoRepositorio;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GrupoResponseDTO> ejecutar(UUID idDocente) {
		return grupoRepositorio.consultarPorDocente(UtilUUID.obtenerValorDefecto(idDocente)).stream()
				.map(grupo -> new GrupoResponseDTO(grupo.getId(), grupo.getNombre(), grupo.getEstado().name(),
						grupo.getFechaInicio(), grupo.getFechaFin()))
				.toList();
	}

}