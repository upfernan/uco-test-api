package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteGrupoJPARepositorio extends JpaRepository<EstudianteGrupoEntidadJPA, UUID> {

	boolean existsByEstudiante_IdAndGrupo_Id(UUID idEstudiante, UUID idGrupo);

}