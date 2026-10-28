package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio.adaptador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.CodigoMensaje;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.propiedades.adaptador.CatalogoMensajesPropiedadesAdaptador;
import co.edu.uco.ucotestapi.infraestructura.serviciosexternos.catalogomensajes.servicio.ClientePlantillasMensajes;

class CatalogoMensajesServicioAdaptadorTest {
	private static final Instant AHORA = Instant.parse("2026-10-28T15:00:00Z");
	private static final UUID ID = UUID.fromString("33333333-3333-3333-3333-111111111111");

	private final ClientePlantillasMensajes cliente = mock(ClientePlantillasMensajes.class);
	private final Clock reloj = mock(Clock.class);
	private final CatalogoMensajesServicioAdaptador catalogo = new CatalogoMensajesServicioAdaptador(cliente, respaldo(), reloj);

	private static CatalogoMensajesPropiedadesAdaptador respaldo() {
		var mensajes = new ResourceBundleMessageSource();
		mensajes.setBasename("mensajes");
		mensajes.setDefaultEncoding("UTF-8");
		return new CatalogoMensajesPropiedadesAdaptador(mensajes);
	}

	@Test
	void usaLaPlantillaDelServicioYReemplazaLosParametros() {
		when(reloj.instant()).thenReturn(AHORA);
		when(cliente.consultarPlantilla("GRUPO_NO_EXISTE")).thenReturn("Grupo {0} no encontrado (servicio)");

		assertEquals("Grupo " + ID + " no encontrado (servicio)",
				catalogo.consultarMensaje(CodigoMensaje.GRUPO_NO_EXISTE, ID).contenido());
	}

	@Test
	void siElServicioNoTieneElCodigoUsaElArchivo() {
		when(reloj.instant()).thenReturn(AHORA);

		assertEquals("Sesión iniciada", catalogo.consultarMensaje(CodigoMensaje.SESION_INICIADA).contenido());
	}

	@Test
	void siElServicioFallaUsaElArchivoYEsperaAntesDeReintentar() {
		when(reloj.instant()).thenReturn(AHORA);
		when(cliente.consultarPlantilla("SESION_INICIADA")).thenThrow(new IllegalStateException("dormido"));

		assertEquals("Sesión iniciada", catalogo.consultarMensaje(CodigoMensaje.SESION_INICIADA).contenido());
		assertEquals("Sesión iniciada", catalogo.consultarMensaje(CodigoMensaje.SESION_INICIADA).contenido());
		verify(cliente, times(1)).consultarPlantilla("SESION_INICIADA");

		when(reloj.instant()).thenReturn(AHORA.plusSeconds(31));
		catalogo.consultarMensaje(CodigoMensaje.SESION_INICIADA);
		verify(cliente, times(2)).consultarPlantilla("SESION_INICIADA");
	}

}
