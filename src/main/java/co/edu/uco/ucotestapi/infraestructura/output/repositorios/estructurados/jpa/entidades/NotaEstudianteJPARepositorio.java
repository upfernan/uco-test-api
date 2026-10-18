package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaEstudianteJPARepositorio extends JpaRepository<NotaEstudianteEntidadJPA, UUID> {

	boolean existsByEstudianteGrupo_Estudiante_IdAndNota_Id(UUID idEstudiante, UUID idNota);

}