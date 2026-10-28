package co.edu.uco.ucotestapi.funcionalidades.grupo.consultargruposdocente.aplicacion.casouso.dominio;

import java.time.LocalDate;
import java.util.UUID;

public record GrupoDocenteDominio(UUID id, String nombre, String estado, LocalDate fechaInicio, LocalDate fechaFin) {

}
