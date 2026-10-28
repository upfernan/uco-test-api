package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.NotaEstudianteEntidadJpa;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotaEstudianteJpaRepositorio extends JpaRepository<NotaEstudianteEntidadJpa, UUID> {

	boolean existsByEstudianteGrupo_Estudiante_IdAndNota_Id(UUID idEstudiante, UUID idNota);

}