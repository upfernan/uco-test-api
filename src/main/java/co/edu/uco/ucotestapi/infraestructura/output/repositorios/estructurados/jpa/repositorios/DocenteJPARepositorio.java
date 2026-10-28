package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.DocenteEntidadJPA;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteJPARepositorio extends JpaRepository<DocenteEntidadJPA, UUID> {

	Optional<DocenteEntidadJPA> findByCorreo(String correo);

}