package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.EstudianteGrupoEntidadJPA;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteGrupoJPARepositorio extends JpaRepository<EstudianteGrupoEntidadJPA, UUID> {

	boolean existsByEstudiante_IdAndGrupo_Id(UUID idEstudiante, UUID idGrupo);

	Optional<EstudianteGrupoEntidadJPA> findByEstudiante_IdAndGrupo_Id(UUID idEstudiante, UUID idGrupo);

	List<EstudianteGrupoEntidadJPA> findByGrupo_Id(UUID idGrupo);

}