package co.edu.uco.ucotestapi.servicios.casosuso.notaestudiante.consultas;

import co.edu.uco.ucotestapi.servicios.input.ports.notaestudiante.ConsultarEstudiantesGrupoCasoUso;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.GrupoEstudianteRepositorio;
import co.edu.uco.ucotestapi.servicios.input.dto.response.EstudianteResponseDTO;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class ConsultarEstudiantesGrupoCasoUsoImpl implements ConsultarEstudiantesGrupoCasoUso {
	private final GrupoEstudianteRepositorio grupoEstudianteRepositorio;

	public ConsultarEstudiantesGrupoCasoUsoImpl(GrupoEstudianteRepositorio grupoEstudianteRepositorio) {
		this.grupoEstudianteRepositorio = grupoEstudianteRepositorio;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstudianteResponseDTO> ejecutar(UUID idGrupo) {
		return grupoEstudianteRepositorio.consultarPorGrupo(UtilUUID.obtenerValorDefecto(idGrupo)).stream()
				.map(matricula -> new EstudianteResponseDTO(matricula.getEstudiante().getId(),
						matricula.getEstudiante().getNombre()))
				.toList();
	}

}