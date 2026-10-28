package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.sql.jpa.entidad.UsuarioEntidadJpa;

@Repository
public interface UsuarioJpaRepositorio extends JpaRepository<UsuarioEntidadJpa, UUID> {

	boolean existsByCorreo(String correo);

	Optional<UsuarioEntidadJpa> findByCorreo(String correo);

}