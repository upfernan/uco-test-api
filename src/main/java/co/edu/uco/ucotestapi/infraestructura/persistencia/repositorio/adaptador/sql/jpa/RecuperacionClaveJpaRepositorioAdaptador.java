package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.RecuperacionClaveRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.RecuperacionClaveEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.RecuperacionClaveJpaRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.RecuperacionClaveEntidadJpa;

@Component
public class RecuperacionClaveJpaRepositorioAdaptador implements RecuperacionClaveRepositorio {
	private final RecuperacionClaveJpaRepositorio repositorio;
	private final MapeadorJpa<RecuperacionClaveEntidad, RecuperacionClaveEntidadJpa> mapeador;

	public RecuperacionClaveJpaRepositorioAdaptador(final RecuperacionClaveJpaRepositorio repositorio,
			final MapeadorJpa<RecuperacionClaveEntidad, RecuperacionClaveEntidadJpa> mapeador) {
		this.repositorio = repositorio;
		this.mapeador = mapeador;
	}

	@Override
	public void crear(final RecuperacionClaveEntidad entidad) {
		repositorio.save(mapeador.ConvertirAEntidadJPA(entidad));
	}

	@Override
	public void modificar(final UUID id, final RecuperacionClaveEntidad entidad) {
		var entidadJPA = mapeador.ConvertirAEntidadJPA(entidad);
		entidadJPA.setId(id);
		repositorio.save(entidadJPA);
	}

	@Override
	public Optional<RecuperacionClaveEntidad> consultarPendientePorUsuario(final UUID idUsuario) {
		return repositorio.findFirstByIdUsuarioAndUsadoFalseOrderByCreadoEnDesc(idUsuario).map(mapeador::ConvertirAEntidad);
	}

	@Override
	public void anularPendientes(final UUID idUsuario) {
		repositorio.anularPendientes(idUsuario);
	}

}
