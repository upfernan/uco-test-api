package co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.casouso.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import co.edu.uco.ucotestapi.aplicacion.puertosalida.identidad.ProveedorIdentidadPuertoSalida;
import co.edu.uco.ucotestapi.funcionalidades.cuenta.restablecerclave.aplicacion.casouso.dominio.RestablecerClaveDominio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.RecuperacionClaveRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.UsuarioRepositorio;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.RecuperacionClaveEntidad;
import co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.entidad.UsuarioEntidad;
import co.edu.uco.ucotestapi.soporte.CatalogoMensajesPrueba;
import co.edu.uco.ucotestapi.transversal.excepcion.UcoTestApiExcepcion;

class RestablecerClaveCasoUsoImplTest {
	private static final String CORREO = "docente@correo.prueba";
	private static final String INVALIDO = "El código no es válido o ya venció. Solicita uno nuevo.";
	private static final UsuarioEntidad USUARIO = UsuarioEntidad.deDocente(UUID.randomUUID(), CORREO, "hash", UUID.randomUUID());

	private final UsuarioRepositorio usuarioRepositorio = mock(UsuarioRepositorio.class);
	private final RecuperacionClaveRepositorio recuperacionRepositorio = mock(RecuperacionClaveRepositorio.class);
	private final ProveedorIdentidadPuertoSalida proveedorIdentidad = mock(ProveedorIdentidadPuertoSalida.class);
	private final RestablecerClaveCasoUsoImpl casoUso = new RestablecerClaveCasoUsoImpl(usuarioRepositorio,
			recuperacionRepositorio, proveedorIdentidad);
	private RecuperacionClaveEntidad recuperacion;

	@BeforeAll
	static void inicializarCatalogo() {
		CatalogoMensajesPrueba.inicializar();
	}

	@BeforeEach
	void prepararCodigoVigente() {
		recuperacion = new RecuperacionClaveEntidad();
		recuperacion.setId(UUID.randomUUID());
		recuperacion.setIdUsuario(USUARIO.id());
		recuperacion.setCodigoHash("hash-del-codigo");
		recuperacion.setExpiraEn(Instant.now().plusSeconds(600));
		when(usuarioRepositorio.consultarPorCorreo(CORREO)).thenReturn(Optional.of(USUARIO));
		when(recuperacionRepositorio.consultarPendientePorUsuario(USUARIO.id())).thenReturn(Optional.of(recuperacion));
		when(proveedorIdentidad.claveCoincide("123456", "hash-del-codigo")).thenReturn(true);
		when(proveedorIdentidad.cifrarClave("Nueva2026")).thenReturn("hash-nuevo");
	}

	@Test
	void conElCodigoCorrectoCambiaLaClaveYGastaElCodigo() {
		casoUso.ejecutar(new RestablecerClaveDominio(CORREO, " 123456 ", "Nueva2026"));

		var usuario = ArgumentCaptor.forClass(UsuarioEntidad.class);
		verify(usuarioRepositorio).modificar(org.mockito.ArgumentMatchers.eq(USUARIO.id()), usuario.capture());
		assertEquals("hash-nuevo", usuario.getValue().claveHash());
		assertTrue(recuperacion.isUsado());
	}

	@Test
	void unCodigoEquivocadoSumaUnIntentoYNoCambiaLaClave() {
		assertEquals(INVALIDO, error(new RestablecerClaveDominio(CORREO, "000000", "Nueva2026")));

		assertEquals(1, recuperacion.getIntentos());
		verify(recuperacionRepositorio).modificar(recuperacion.getId(), recuperacion);
		verify(usuarioRepositorio, never()).modificar(any(), any());
	}

	@Test
	void rechazaUnCodigoVencidoOConDemasiadosIntentos() {
		recuperacion.setExpiraEn(Instant.now().minusSeconds(1));
		assertEquals(INVALIDO, error(new RestablecerClaveDominio(CORREO, "123456", "Nueva2026")));

		recuperacion.setExpiraEn(Instant.now().plusSeconds(600));
		recuperacion.setIntentos(5);
		assertEquals(INVALIDO, error(new RestablecerClaveDominio(CORREO, "123456", "Nueva2026")));
		verify(usuarioRepositorio, never()).modificar(any(), any());
	}

	@Test
	void rechazaCorreosSinCuentaOSinCodigoConElMismoMensaje() {
		assertEquals(INVALIDO, error(new RestablecerClaveDominio("nadie@correo.prueba", "123456", "Nueva2026")));

		when(recuperacionRepositorio.consultarPendientePorUsuario(USUARIO.id())).thenReturn(Optional.empty());
		assertEquals(INVALIDO, error(new RestablecerClaveDominio(CORREO, "123456", "Nueva2026")));
	}

	@Test
	void laClaveNuevaDebeSerSegura() {
		assertTrue(error(new RestablecerClaveDominio(CORREO, "123456", "corta")).contains("mínimo 8 caracteres"));
		verify(usuarioRepositorio, never()).modificar(any(), any());
	}

	private String error(final RestablecerClaveDominio datos) {
		return assertThrows(UcoTestApiExcepcion.class, () -> casoUso.ejecutar(datos)).getMensajeUsuario();
	}

}
