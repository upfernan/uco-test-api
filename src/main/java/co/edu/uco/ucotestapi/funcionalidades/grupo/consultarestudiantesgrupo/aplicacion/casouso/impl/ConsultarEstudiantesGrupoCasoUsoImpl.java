package co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.casouso.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.casouso.ConsultarEstudiantesGrupoCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.casouso.dominio.EstudianteDelGrupoDominio;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.casouso.impl.mapeador.ConsultarEstudiantesGrupoMapeadorCasoUso;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.EstudianteGrupoRepositorio;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class ConsultarEstudiantesGrupoCasoUsoImpl implements ConsultarEstudiantesGrupoCasoUso {
	private final EstudianteGrupoRepositorio repositorio;
	private final ConsultarEstudiantesGrupoMapeadorCasoUso mapeador;

	public ConsultarEstudiantesGrupoCasoUsoImpl(final EstudianteGrupoRepositorio repositorio, final ConsultarEstudiantesGrupoMapeadorCasoUso mapeador) {
		this.repositorio = repositorio;
		this.mapeador = mapeador;
	}

	@Override
	public List<EstudianteDelGrupoDominio> ejecutar(final UUID idGrupo) {
		return repositorio.consultarPorGrupo(UtilUUID.obtenerValorDefecto(idGrupo)).stream().map(mapeador::aDominio).toList();
	}

}
