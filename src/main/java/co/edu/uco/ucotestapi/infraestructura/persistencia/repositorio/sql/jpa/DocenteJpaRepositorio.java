package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.DocenteEntidadJpa;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DocenteJpaRepositorio extends JpaRepository<DocenteEntidadJpa, UUID> {

	Optional<DocenteEntidadJpa> findByCorreo(String correo);

}