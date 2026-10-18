package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEstudianteEntidad;

public interface NotaEstudianteRepositorio extends CrearRepositorio<NotaEstudianteEntidad>, ConsultarRepositorio<UUID, NotaEstudianteEntidad>, ModificarRepositorio<UUID, NotaEstudianteEntidad>, EliminarRepositorio<UUID> {

	boolean existePorEstudianteYNota(UUID idEstudiante, UUID idNota);

}