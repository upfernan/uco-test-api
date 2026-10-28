package co.edu.uco.ucotestapi.aplicacion.casouso;

public interface CasoUso<D, R> {

	R ejecutar(D datos);

}
