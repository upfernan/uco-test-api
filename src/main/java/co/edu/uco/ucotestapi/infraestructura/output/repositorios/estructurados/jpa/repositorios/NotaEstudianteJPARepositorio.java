package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.NotaEstudianteEntidadJPA;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaEstudianteJPARepositorio extends JpaRepository<NotaEstudianteEntidadJPA, UUID> {

	boolean existsByEstudianteGrupo_Estudiante_IdAndNota_Id(UUID idEstudiante, UUID idNota);

}