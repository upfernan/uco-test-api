package co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.ConsultarGruposDocenteCasoUso;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.dominio.GrupoDocenteDominio;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.impl.mapeador.ConsultarGruposDocenteMapeadorCasoUso;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.GrupoRepositorio;
import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

@Service
public class ConsultarGruposDocenteCasoUsoImpl implements ConsultarGruposDocenteCasoUso {
	private final GrupoRepositorio repositorio;
	private final ConsultarGruposDocenteMapeadorCasoUso mapeador;

	public ConsultarGruposDocenteCasoUsoImpl(final GrupoRepositorio repositorio, final ConsultarGruposDocenteMapeadorCasoUso mapeador) {
		this.repositorio = repositorio;
		this.mapeador = mapeador;
	}

	@Override
	public List<GrupoDocenteDominio> ejecutar(final UUID idDocente) {
		return repositorio.consultarPorDocente(UtilUUID.obtenerValorDefecto(idDocente)).stream().map(mapeador::aDominio).toList();
	}

}
