package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato;

public interface ModificarRepositorio<I, E> {

	void modificar(I id, E entidad);

}
