package co.edu.uco.ucotestapi.servicios.output.ports.serviciosexternos.identityprovided;

import java.time.Instant;

public record TokenDTO(String token, Instant expiraEn) {

}