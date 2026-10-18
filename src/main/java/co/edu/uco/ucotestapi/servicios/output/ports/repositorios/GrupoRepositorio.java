package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.GrupoEntidad;

public interface GrupoRepositorio extends CrearRepositorio<GrupoEntidad>, ConsultarRepositorio<UUID, GrupoEntidad>, ActualizarRepositorio<GrupoEntidad>, EliminarRepositorio<UUID> {
	
}