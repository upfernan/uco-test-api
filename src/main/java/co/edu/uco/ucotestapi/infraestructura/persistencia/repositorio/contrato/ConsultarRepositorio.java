package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.contrato;

import java.util.List;
import java.util.Optional;

public interface ConsultarRepositorio<I, E> {

	List<E> consultarTodos();

	List<E> consultar(E filtro);

	Optional<E> consultarPorId(I id);

	boolean existePorId(I id);

}
