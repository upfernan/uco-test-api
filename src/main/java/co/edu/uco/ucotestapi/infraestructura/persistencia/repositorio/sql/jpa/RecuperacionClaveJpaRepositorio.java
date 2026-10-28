package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.RecuperacionClaveEntidadJpa;

@Repository
public interface RecuperacionClaveJpaRepositorio extends JpaRepository<RecuperacionClaveEntidadJpa, UUID> {

	Optional<RecuperacionClaveEntidadJpa> findFirstByIdUsuarioAndUsadoFalseOrderByCreadoEnDesc(UUID idUsuario);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("update RecuperacionClaveEntidadJpa r set r.usado = true where r.idUsuario = :idUsuario and r.usado = false")
	void anularPendientes(@Param("idUsuario") UUID idUsuario);

}
