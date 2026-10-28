package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ModificarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.CrearRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.EliminarRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato.ConsultarRepositorio;

import java.util.UUID;

import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.NotaEstudianteEntidad;

public interface NotaEstudianteRepositorio extends CrearRepositorio<NotaEstudianteEntidad>, ConsultarRepositorio<UUID, NotaEstudianteEntidad>, ModificarRepositorio<UUID, NotaEstudianteEntidad>, EliminarRepositorio<UUID> {

	boolean existePorEstudianteYNota(UUID idEstudiante, UUID idNota);

}