package co.edu.uco.ucotestapi.aplicacion.puertoentrada;

public interface PuertoEntrada<T, R> {

	R ejecutar(T datos);

}
