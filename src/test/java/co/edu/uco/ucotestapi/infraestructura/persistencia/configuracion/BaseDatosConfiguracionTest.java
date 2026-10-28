package co.edu.uco.ucotestapi.infraestructura.persistencia.configuracion;

import co.edu.uco.ucotestapi.infraestructura.persistencia.configuracion.BaseDatosConfiguracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariDataSource;

class BaseDatosConfiguracionTest {

	private final BaseDatosConfiguracion configuracion = new BaseDatosConfiguracion();

	@Test
	void creaElDataSourceConLasCredencialesDeLaCadena() {
		var dataSource = assertInstanceOf(HikariDataSource.class, configuracion
				.dataSource("postgresql://neondb_owner:npg_Clave123@ep-host.aws.neon.tech/neondb?sslmode=require"));

		try (dataSource) {
			assertEquals("jdbc:postgresql://ep-host.aws.neon.tech:5432/neondb?sslmode=require", dataSource.getJdbcUrl());
			assertEquals("neondb_owner", dataSource.getUsername());
			assertEquals("npg_Clave123", dataSource.getPassword());
		}
	}

	@Test
	void usaLaCadenaJdbcTalCualSinCredencialesAparte() {
		var jdbc = "jdbc:postgresql://host:5432/base?user=u&password=p";
		var dataSource = assertInstanceOf(HikariDataSource.class, configuracion.dataSource(jdbc));

		try (dataSource) {
			assertEquals(jdbc, dataSource.getJdbcUrl());
			assertEquals(null, dataSource.getUsername());
		}
	}

}
