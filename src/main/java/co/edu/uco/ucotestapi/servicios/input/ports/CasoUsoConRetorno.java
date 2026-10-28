package co.edu.uco.ucotestapi.servicios.input.ports;

public interface CasoUsoConRetorno<O, I> {
	O ejecutar(I datos);

}
