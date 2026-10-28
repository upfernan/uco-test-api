package co.edu.uco.ucotestapi.aplicacion.casouso.especificacion.generales;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilUUID;

class EspecificacionesGeneralesTest {

	@Test
	void textoPresente() {
		assertTrue(new TextoPresenteEspecificacion().esSatisfechaPor(" a "));
		assertFalse(new TextoPresenteEspecificacion().esSatisfechaPor("   "));
		assertFalse(new TextoPresenteEspecificacion().esSatisfechaPor(null));
	}

	@Test
	void longitudTexto() {
		var especificacion = new LongitudTextoEspecificacion(2, 4);
		assertTrue(especificacion.esSatisfechaPor(" abc "));
		assertFalse(especificacion.esSatisfechaPor("a"));
		assertFalse(especificacion.esSatisfechaPor("abcde"));
	}

	@Test
	void formatoYContenidoDeTexto() {
		assertTrue(new FormatoTextoEspecificacion(Pattern.compile("^\\d++$")).esSatisfechaPor(" 123 "));
		assertFalse(new FormatoTextoEspecificacion(Pattern.compile("^\\d++$")).esSatisfechaPor("12a"));
		assertTrue(new ContieneTextoEspecificacion(Pattern.compile("\\d")).esSatisfechaPor("ab1"));
		assertFalse(new ContieneTextoEspecificacion(Pattern.compile("\\d")).esSatisfechaPor(null));
	}

	@Test
	void uuidPresente() {
		assertTrue(new UUIDPresenteEspecificacion().esSatisfechaPor(UUID.randomUUID()));
		assertFalse(new UUIDPresenteEspecificacion().esSatisfechaPor(UtilUUID.UUID_DEFECTO));
		assertFalse(new UUIDPresenteEspecificacion().esSatisfechaPor(null));
	}

	@Test
	void enteroEnRango() {
		var rango = new EnteroEnRangoEspecificacion(0, 500);
		assertTrue(rango.esSatisfechaPor(0));
		assertTrue(rango.esSatisfechaPor(500));
		assertFalse(rango.esSatisfechaPor(501));
		assertFalse(rango.esSatisfechaPor(null));
	}

	@Test
	void fechaEnRango() {
		var hoy = LocalDate.now();
		assertTrue(new FechaEnRangoEspecificacion(hoy.minusDays(1), hoy.plusDays(1)).esSatisfechaPor(hoy));
		assertFalse(new FechaEnRangoEspecificacion(hoy.plusDays(1), hoy.plusDays(2)).esSatisfechaPor(hoy));
		assertFalse(new FechaEnRangoEspecificacion(null, hoy).esSatisfechaPor(hoy));
	}

	@Test
	void instanteEnRango() {
		var ahora = Instant.now();
		assertTrue(new InstanteEnRangoEspecificacion(ahora.minusSeconds(60), ahora.plusSeconds(60)).esSatisfechaPor(ahora));
		assertFalse(new InstanteEnRangoEspecificacion(ahora.minusSeconds(60), ahora.minusSeconds(1)).esSatisfechaPor(ahora));
		assertFalse(new InstanteEnRangoEspecificacion(ahora, null).esSatisfechaPor(ahora));
	}

	@Test
	void esIgual() {
		assertTrue(new EsIgualEspecificacion("A").esSatisfechaPor("A"));
		assertFalse(new EsIgualEspecificacion("A").esSatisfechaPor(null));
	}

}