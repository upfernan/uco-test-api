package co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad;

import java.time.Instant;

public record TokenDTO(String token, Instant expiraEn) {

}