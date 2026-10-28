package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.NotaEntidadJpa;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotaJpaRepositorio extends JpaRepository<NotaEntidadJpa, UUID> {

	List<NotaEntidadJpa> findByGrupo_Id(UUID idGrupo);

}