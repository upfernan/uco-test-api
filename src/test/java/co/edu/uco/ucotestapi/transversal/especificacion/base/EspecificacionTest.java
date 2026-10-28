package co.edu.uco.ucotestapi.transversal.especificacion.base;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EspecificacionTest {

	private static final Especificacion<Integer> POSITIVO = new Especificacion<>() {
		@Override
		public boolean esSatisfechaPor(final Integer dato) {
			return dato > 0;
		}
	};
	private static final Especificacion<Integer> PAR = new Especificacion<>() {
		@Override
		public boolean esSatisfechaPor(final Integer dato) {
			return dato % 2 == 0;
		}
	};

	@Test
	void yExigeQueSeCumplanLasDos() {
		assertTrue(POSITIVO.y(PAR).esSatisfechaPor(4));
		assertFalse(POSITIVO.y(PAR).esSatisfechaPor(3));
	}

	@Test
	void oBastaConQueSeCumplaUna() {
		assertTrue(POSITIVO.o(PAR).esSatisfechaPor(-2));
		assertFalse(POSITIVO.o(PAR).esSatisfechaPor(-3));
	}

	@Test
	void noNiegaLaEspecificacion() {
		assertTrue(POSITIVO.no().esSatisfechaPor(-1));
		assertFalse(POSITIVO.no().esSatisfechaPor(1));
	}

}