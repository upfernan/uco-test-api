package co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.ConsultarNotasGrupoCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.dominio.NotaDelGrupoDominio;
import co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.impl.mapeador.ConsultarNotasGrupoMapeadorCasoUso;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.NotaRepositorio;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class ConsultarNotasGrupoCasoUsoImpl implements ConsultarNotasGrupoCasoUso {
	private final NotaRepositorio repositorio;
	private final ConsultarNotasGrupoMapeadorCasoUso mapeador;

	public ConsultarNotasGrupoCasoUsoImpl(final NotaRepositorio repositorio, final ConsultarNotasGrupoMapeadorCasoUso mapeador) {
		this.repositorio = repositorio;
		this.mapeador = mapeador;
	}

	@Override
	public List<NotaDelGrupoDominio> ejecutar(final UUID idGrupo) {
		return repositorio.consultarPorGrupo(UtilUUID.obtenerValorDefecto(idGrupo)).stream().map(mapeador::aDominio).toList();
	}

}
