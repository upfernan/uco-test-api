package co.edu.uco.ucotestapi.servicios.output.ports.repositorios;

public interface ModificarRepositorio<I, E> {

	void modificar(I id, E entidad);

}
