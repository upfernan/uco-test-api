package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.GrupoEntidadJPA;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoJPARepositorio extends JpaRepository<GrupoEntidadJPA, UUID> {

	List<GrupoEntidadJPA> findByDocente_Id(UUID idDocente);

}