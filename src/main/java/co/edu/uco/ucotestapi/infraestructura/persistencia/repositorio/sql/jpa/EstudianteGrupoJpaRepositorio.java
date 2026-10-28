package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.EstudianteGrupoEntidadJpa;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteGrupoJpaRepositorio extends JpaRepository<EstudianteGrupoEntidadJpa, UUID> {

	boolean existsByEstudiante_IdAndGrupo_Id(UUID idEstudiante, UUID idGrupo);

	Optional<EstudianteGrupoEntidadJpa> findByEstudiante_IdAndGrupo_Id(UUID idEstudiante, UUID idGrupo);

	List<EstudianteGrupoEntidadJpa> findByGrupo_Id(UUID idGrupo);

}