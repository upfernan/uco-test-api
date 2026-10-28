package co.edu.uco.ucotestapi.infraestructura.persistencia.configuracion;

import co.edu.uco.ucotestapi.infraestructura.persistencia.configuracion.UrlBaseDatos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UrlBaseDatosTest {

	private static final String NEON = "postgresql://neondb_owner:npg_Clave123@ep-cool-sun-a1b2c3.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require";
	private static final String JDBC_NEON = "jdbc:postgresql://ep-cool-sun-a1b2c3.us-east-1.aws.neon.tech:5432/neondb?sslmode=require";

	@Test
	void convierteLaCadenaDeNeonYQuitaChannelBinding() {
		var url = UrlBaseDatos.desde(NEON);

		assertEquals(JDBC_NEON, url.jdbcUrl());
		assertEquals("neondb_owner", url.usuario());
		assertEquals("npg_Clave123", url.clave());
	}

	@Test
	void aceptaElComandoPsqlConComillasQueCopiaLaConsolaDeNeon() {
		var url = UrlBaseDatos.desde("psql '" + NEON + "'");

		assertEquals(JDBC_NEON, url.jdbcUrl());
		assertEquals("npg_Clave123", url.clave());
	}

	@Test
	void aceptaComillasDoblesYEspacios() {
		assertEquals(JDBC_NEON, UrlBaseDatos.desde("  \"" + NEON + "\"  ").jdbcUrl());
	}

	@Test
	void aceptaElFormatoJdbcSinTocarlo() {
		var jdbc = "jdbc:postgresql://host:5432/base?user=u&password=p&sslmode=require";
		var url = UrlBaseDatos.desde(jdbc);

		assertEquals(jdbc, url.jdbcUrl());
		assertFalse(url.tieneCredenciales());
	}

	@Test
	void respetaElPuertoYLaClaveCodificada() {
		var url = UrlBaseDatos.desde("postgres://usuario:cl%40ve+1@localhost:5433/ucotestapi");

		assertEquals("jdbc:postgresql://localhost:5433/ucotestapi", url.jdbcUrl());
		assertEquals("cl@ve+1", url.clave());
		assertTrue(url.tieneCredenciales());
	}

	@Test
	void rechazaUnFormatoDesconocidoSinMostrarLaClave() {
		var error = assertThrows(IllegalStateException.class, () -> UrlBaseDatos.desde("mysql://u:secreta@host/db"));

		assertFalse(error.getMessage().contains("secreta"));
	}

	@Test
	void rechazaCaracteresNoValidosSinMostrarLaClave() {
		var error = assertThrows(IllegalStateException.class,
				() -> UrlBaseDatos.desde("postgresql://u:secreta@ho^st/db"));

		assertFalse(error.getMessage().contains("secreta"));
	}

	@Test
	void aceptaElFormatoEnvQueCopiaNeon() {
		var url = UrlBaseDatos.desde("DATABASE_URL='" + NEON + "'");

		assertEquals(JDBC_NEON, url.jdbcUrl());
		assertEquals("npg_Clave123", url.clave());
	}

	@Test
	void aceptaLaCadenaConSaltosDeLinea() {
		assertEquals(JDBC_NEON, UrlBaseDatos.desde("\n# Neon\nDATABASE_URL=\"" + NEON + "\"\n").jdbcUrl());
	}

	@Test
	void aceptaLasVariablesPgDeNeon() {
		var url = UrlBaseDatos.desde("""
				PGHOST='ep-cool-sun-a1b2c3.us-east-1.aws.neon.tech'
				PGDATABASE='neondb'
				PGUSER='neondb_owner'
				PGPASSWORD='npg_Clave123'
				""");

		assertEquals(JDBC_NEON, url.jdbcUrl());
		assertEquals("neondb_owner", url.usuario());
		assertEquals("npg_Clave123", url.clave());
	}

	@Test
	void rechazaVariablesPgIncompletas() {
		var error = assertThrows(IllegalStateException.class, () -> UrlBaseDatos.desde("PGHOST=host-secreto"));

		assertFalse(error.getMessage().contains("host-secreto"));
	}

	@Test
	void rechazaUnValorVacio() {
		var error = assertThrows(IllegalStateException.class, () -> UrlBaseDatos.desde("   "));

		assertTrue(error.getMessage().contains("vacía"));
	}

	@Test
	void describeLoRecibidoSinMostrarlo() {
		var error = assertThrows(IllegalStateException.class, () -> UrlBaseDatos.desde("npg_SoloLaClave"));

		assertTrue(error.getMessage().contains("15 caracteres"));
		assertFalse(error.getMessage().contains("npg_SoloLaClave"));
	}

	@Test
	void rechazaUnaCadenaSinCredencialesSinMostrarla() {
		var error = assertThrows(IllegalStateException.class, () -> UrlBaseDatos.desde("postgresql://host-secreto/db"));

		assertFalse(error.getMessage().contains("host-secreto"));
	}

}
