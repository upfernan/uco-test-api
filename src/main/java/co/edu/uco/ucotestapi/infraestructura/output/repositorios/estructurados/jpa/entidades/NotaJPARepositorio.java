package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.jpa.entidades;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaJPARepositorio extends JpaRepository<NotaEntidadJPA, UUID> {

}