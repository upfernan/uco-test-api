package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

import java.util.UUID;

import co.edu.uco.ucotestapi.servicios.output.ports.repositorios.entidades.DocenteEntidad;

public interface DocenteRepositorio extends CrearRepositorio<DocenteEntidad>, ConsultarRepositorio<UUID, DocenteEntidad>, ModificarRepositorio<UUID, DocenteEntidad>, EliminarRepositorio<UUID> {
	
}