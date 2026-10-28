package co.edu.uco.ucotestapi.funcionalidades.nota.consultarnotasgrupo.aplicacion.casouso.dominio;

import java.time.Instant;
import java.util.UUID;

public record NotaDelGrupoDominio(UUID id, String nombre, String estado, Instant fechaRegistroDesde, Instant fechaRegistroHasta) {

}
