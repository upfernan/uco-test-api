package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio;

import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.CrearRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ModificarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.RecuperacionClaveEntidad;

public interface RecuperacionClaveRepositorio
		extends CrearRepositorio<RecuperacionClaveEntidad>, ModificarRepositorio<UUID, RecuperacionClaveEntidad> {

	/** El codigo mas reciente del usuario que todavia no se ha usado. */
	Optional<RecuperacionClaveEntidad> consultarPendientePorUsuario(UUID idUsuario);

	/** Marca como usados los codigos pendientes del usuario, para que solo sirva el ultimo. */
	void anularPendientes(UUID idUsuario);

}
