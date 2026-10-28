package co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador;

public interface MapeadorJpa<E, J> {

	E ConvertirAEntidad(J entidadJPA);

	J ConvertirAEntidadJPA(E entidad);

}