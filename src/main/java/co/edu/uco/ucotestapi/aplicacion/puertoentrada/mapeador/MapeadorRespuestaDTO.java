package co.edu.uco.ucotestapi.aplicacion.puertoentrada.mapeador;

public interface MapeadorRespuestaDTO<M, D> {

	D aDTO(M dominio);

}
