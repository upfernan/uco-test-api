package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.NotaEntidadJpa;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaJpaRepositorio extends JpaRepository<NotaEntidadJpa, UUID> {

	List<NotaEntidadJpa> findByGrupo_Id(UUID idGrupo);

}