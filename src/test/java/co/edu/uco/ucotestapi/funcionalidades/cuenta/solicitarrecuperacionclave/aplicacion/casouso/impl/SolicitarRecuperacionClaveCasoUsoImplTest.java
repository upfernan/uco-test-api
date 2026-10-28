package co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.casouso.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.casouso.dominio.SolicitarRecuperacionClaveDominio;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertosalida.CodigoRecuperacionDTO;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.solicitarrecuperacionclave.aplicacion.puertosalida.CodigoRecuperacionPuertoSalida;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.RecuperacionClaveRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.RecuperacionClaveEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;
import co.edu.uco.ucotestapi.soporte.CatalogoMensajesPrueba;

class SolicitarRecuperacionClaveCasoUsoImplTest {
	private static final String CORREO = "docente@correo.prueba";
	private static final UsuarioEntidad USUARIO = UsuarioEntidad.deDocente(UUID.randomUUID(), CORREO, "hash", UUID.randomUUID());

	private final UsuarioRepositorio usuarioRepositorio = mock(UsuarioRepositorio.class);
	private final RecuperacionClaveRepositorio recuperacionRepositorio = mock(RecuperacionClaveRepositorio.class);
	private final ProveedorIdentidadPuertoSalida proveedorIdentidad = mock(ProveedorIdentidadPuertoSalida.class);
	private final CodigoRecuperacionPuertoSalida envioCodigo = mock(CodigoRecuperacionPuertoSalida.class);
	private final SolicitarRecuperacionClaveCasoUsoImpl casoUso = new SolicitarRecuperacionClaveCasoUsoImpl(
			usuarioRepositorio, recuperacionRepositorio, proveedorIdentidad, envioCodigo);

	@BeforeAll
	static void inicializarCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	@Test
	void conCuentaGuardaElCodigoCifradoYLoEnvia() {
		when(usuarioRepositorio.consultarPorCorreo(CORREO)).thenReturn(Optional.of(USUARIO));
		when(proveedorIdentidad.cifrarClave(anyString())).thenAnswer(invocacion -> "cifrado:" + invocacion.getArgument(0));

		casoUso.ejecutar(new SolicitarRecuperacionClaveDominio("  Docente@Correo.Prueba "));

		verify(recuperacionRepositorio).anularPendientes(USUARIO.id());
		var guardada = ArgumentCaptor.forClass(RecuperacionClaveEntidad.class);
		verify(recuperacionRepositorio).crear(guardada.capture());
		var enviado = ArgumentCaptor.forClass(CodigoRecuperacionDTO.class);
		verify(envioCodigo).enviar(enviado.capture());

		var codigo = guardada.getValue().getCodigoHash().replace("cifrado:", "");
		assertTrue(codigo.matches("\\d{6}"), "El codigo debe tener 6 digitos");
		assertTrue(enviado.getValue().contenido().contains(codigo));
		assertEquals(CORREO, enviado.getValue().correoDestinatario());
		assertEquals(USUARIO.id(), guardada.getValue().getIdUsuario());
		assertFalse(guardada.getValue().isUsado());
		var vigencia = Duration.between(Instant.now(), guardada.getValue().getExpiraEn());
		assertTrue(vigencia.toMinutes() >= 14 && vigencia.toMinutes() <= 15, "Debe vencer en 15 minutos");
	}

	@Test
	void sinCuentaNoHaceNadaParaNoRevelarQueCorreosExisten() {
		when(usuarioRepositorio.consultarPorCorreo(any())).thenReturn(Optional.empty());

		casoUso.ejecutar(new SolicitarRecuperacionClaveDominio("nadie@correo.prueba"));

		verify(recuperacionRepositorio, never()).crear(any());
		verify(envioCodigo, never()).enviar(any());
	}

}
