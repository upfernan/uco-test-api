package co.edu.uco.ucotestapi.funcionalidades.notaestudiante.registrarnotaestudiante.aplicacion.casouso.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEstudianteEntidad;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Component
public class RegistrarNotaEstudianteMapeadorCasoUso {

	public NotaEstudianteEntidad aEntidad(final int valorEscalado, final EstudianteGrupoEntidad estudianteGrupo,
			final NotaEntidad nota) {
		var entidad = new NotaEstudianteEntidad();
		entidad.setId(UtilUUID.generar());
		entidad.setEstudianteGrupo(estudianteGrupo);
		entidad.setNota(nota);
		entidad.setValor(valorEscalado);
		return entidad;
	}

}