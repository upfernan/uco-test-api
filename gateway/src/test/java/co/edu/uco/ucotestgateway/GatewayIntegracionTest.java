package co.edu.uco.ucotestgateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.sun.net.httpserver.HttpServer;

/**
 * Levanta una API falsa y revisa que el gateway reenvie, marque las respuestas y aplique el limite de peticiones.
 * El limite usa el Redis de docker-compose (o el del CI).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayIntegracionTest {
	private static final HttpServer API_FALSA = iniciarApiFalsa();
	private static final HttpClient CLIENTE = HttpClient.newHttpClient();

	@LocalServerPort
	private int puerto;

	@DynamicPropertySource
	static void apuntarAApiFalsa(final DynamicPropertyRegistry registro) {
		registro.add("api.url", () -> "http://localhost:" + API_FALSA.getAddress().getPort());
	}

	@AfterAll
	static void apagarApiFalsa() {
		API_FALSA.stop(0);
	}

	@Test
	void reenviaLasPeticionesDeLaApiYMarcaLaRespuesta() throws Exception {
		var respuesta = enviar(HttpRequest.newBuilder(uri("/api/v1/grupos/1/notas")).GET(), "10.0.0.1");

		assertEquals(200, respuesta.statusCode());
		assertEquals("GET /api/v1/grupos/1/notas", respuesta.body());
		assertEquals("uco-test-gateway", respuesta.headers().firstValue("X-Gateway").orElse(""));
	}

	@Test
	void exponeLaSaludDeLaApi() throws Exception {
		var respuesta = enviar(HttpRequest.newBuilder(uri("/salud-api")).GET(), "10.0.0.2");

		assertEquals("GET /actuator/health", respuesta.body());
	}

	@Test
	void reenviaLaPaginaWeb() throws Exception {
		var respuesta = enviar(HttpRequest.newBuilder(uri("/app.js")).GET(), "10.0.0.4");

		assertEquals("GET /app.js", respuesta.body());
	}

	@Test
	void noReenviaRutasFueraDeLaApi() throws Exception {
		var respuesta = enviar(HttpRequest.newBuilder(uri("/otra-cosa")).GET(), "10.0.0.3");

		assertEquals(404, respuesta.statusCode());
	}

	@Test
	void limitaLosIntentosDeIniciarSesionPorIp() throws Exception {
		var ip = "10.1." + (UUID.randomUUID().hashCode() & 0xff) + ".1";
		var bloqueadas = 0;
		for (var intento = 0; intento < 20; intento++) {
			var respuesta = enviar(HttpRequest.newBuilder(uri("/api/v1/auth/login"))
					.POST(HttpRequest.BodyPublishers.ofString("{}")), ip);
			if (respuesta.statusCode() == 429) {
				bloqueadas++;
			}
		}

		assertTrue(bloqueadas > 0, "Con 20 intentos seguidos alguno debe responder 429");
	}

	private URI uri(final String ruta) {
		return URI.create("http://localhost:" + puerto + ruta);
	}

	private static HttpResponse<String> enviar(final HttpRequest.Builder peticion, final String ip)
			throws IOException, InterruptedException {
		return CLIENTE.send(peticion.header("X-Forwarded-For", ip).build(), HttpResponse.BodyHandlers.ofString());
	}

	private static HttpServer iniciarApiFalsa() {
		try {
			var servidor = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
			servidor.createContext("/", intercambio -> {
				var cuerpo = (intercambio.getRequestMethod() + " " + intercambio.getRequestURI().getPath())
						.getBytes(StandardCharsets.UTF_8);
				intercambio.sendResponseHeaders(200, cuerpo.length);
				intercambio.getResponseBody().write(cuerpo);
				intercambio.close();
			});
			servidor.start();
			return servidor;
		} catch (IOException excepcion) {
			throw new IllegalStateException("No se pudo levantar la API falsa", excepcion);
		}
	}

}
