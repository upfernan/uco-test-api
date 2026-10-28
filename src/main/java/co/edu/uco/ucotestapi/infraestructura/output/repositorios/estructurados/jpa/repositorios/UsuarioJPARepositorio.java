package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.repositorios;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades.UsuarioEntidadJPA;

@Repository
public interface UsuarioJPARepositorio extends JpaRepository<UsuarioEntidadJPA, UUID> {

	boolean existsByCorreo(String correo);

	Optional<UsuarioEntidadJPA> findByCorreo(String correo);

}