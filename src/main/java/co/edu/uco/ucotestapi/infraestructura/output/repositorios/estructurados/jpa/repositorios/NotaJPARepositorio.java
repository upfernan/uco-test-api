package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.NotaEntidadJPA;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaJPARepositorio extends JpaRepository<NotaEntidadJPA, UUID> {

	List<NotaEntidadJPA> findByGrupo_Id(UUID idGrupo);

}