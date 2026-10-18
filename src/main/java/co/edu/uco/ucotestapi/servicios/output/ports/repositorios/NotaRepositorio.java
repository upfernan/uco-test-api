package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.NotaEntidad;

public interface NotaRepositorio extends CrearRepositorio<NotaEntidad>, ConsultarRepositorio<UUID, NotaEntidad>, ActualizarRepositorio<NotaEntidad>, EliminarRepositorio<UUID> {
	
}