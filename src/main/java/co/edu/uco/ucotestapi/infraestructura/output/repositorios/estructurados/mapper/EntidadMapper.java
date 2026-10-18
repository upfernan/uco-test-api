package co.edu.uco.ucotestapi.infraestructura.output.repositorios.estructurados.mapper;

public interface EntidadMapper<E, J> {

	E ConvertirAEntidad(J entidadJPA);

	J ConvertirAEntidadJPA(E entidad);

}