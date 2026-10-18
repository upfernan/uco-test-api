package co.edu.uco.ucotestapi.servicios.casosuso;

public interface CasoUsoConRetorno<O, I> {
	O ejecutar(I datos);

}
