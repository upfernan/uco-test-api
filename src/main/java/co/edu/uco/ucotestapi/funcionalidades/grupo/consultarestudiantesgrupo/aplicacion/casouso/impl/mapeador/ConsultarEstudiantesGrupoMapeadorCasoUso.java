package co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.casouso.impl.mapeador;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.aplicacion.casouso.mapeador.MapeadorDominio;
import co.edu.uco.ucotestapi.funcionalidades.grupo.consultarestudiantesgrupo.aplicacion.casouso.dominio.EstudianteDelGrupoDominio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.EstudianteGrupoEntidad;

@Component
public class ConsultarEstudiantesGrupoMapeadorCasoUso implements MapeadorDominio<EstudianteDelGrupoDominio, EstudianteGrupoEntidad> {

	@Override
	public EstudianteDelGrupoDominio aDominio(final EstudianteGrupoEntidad entidad) {
		return new EstudianteDelGrupoDominio(entidad.getEstudiante().getId(), entidad.getEstudiante().getNombre());
	}

}
