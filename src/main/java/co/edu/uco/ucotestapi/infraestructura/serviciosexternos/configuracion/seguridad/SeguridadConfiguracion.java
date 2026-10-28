package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.seguridad;

import co.edu.uco.ucotestapi.transversal.respuesta.RespuestaError;
import co.edu.uco.ucotestapi.infraestructura.persistencia.configuracion.UrlBaseDatos;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;

import co.edu.uco.ucotestapi.transversal.utilitario.UtilTexto;

@Configuration
public class SeguridadConfiguracion {
	private static final String[] RUTAS_PUBLICAS = { "/actuator/health/**", "/api/v1/auth/**", "/error" };
	private static final String[] PAGINAS_PUBLICAS = { "/", "/index.html", "/*.css", "/*.js", "/favicon.ico" };

	@Bean
	public SecretKey claveJwt(@Value("${JWT_SECRET:}") final String secreto,
			@Value("${DATABASE_URL:}") final String urlBaseDatos) {
		return ClaveJwt.desde(secreto, UtilTexto.tieneContenido(urlBaseDatos));
	}

	@Bean
	public JwtEncoder codificadorTokens(final SecretKey claveJwt) {
		return new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(claveJwt));
	}

	@Bean
	public JwtDecoder decodificadorTokens(final SecretKey claveJwt) {
		return NimbusJwtDecoder.withSecretKey(claveJwt).macAlgorithm(MacAlgorithm.HS256).build();
	}

	@Bean
	public PasswordEncoder codificadorClaves() {
		return new BCryptPasswordEncoder();
	}

	// CSRF solo aplica cuando el navegador envia credenciales automaticamente (cookies de sesion).
	// Esta API es sin estado y la sesion viaja en el encabezado Authorization: Bearer, que el navegador
	// nunca adjunta por su cuenta, asi que un sitio malicioso no puede falsificar peticiones autenticadas.
	@SuppressWarnings("java:S4502")
	@Bean
	public SecurityFilterChain cadenaSeguridad(final HttpSecurity http, final RespuestaErrorSeguridad respuestaError) {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(rutas -> rutas
						.requestMatchers(RUTAS_PUBLICAS).permitAll()
						.requestMatchers(HttpMethod.GET, PAGINAS_PUBLICAS).permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/notas-estudiantes").hasRole("DOCENTE")
						.anyRequest().authenticated())
				.oauth2ResourceServer(servidor -> servidor
						.jwt(jwt -> jwt.jwtAuthenticationConverter(convertidorRoles()))
						.authenticationEntryPoint(respuestaError)
						.accessDeniedHandler(respuestaError))
				.exceptionHandling(errores -> errores
						.authenticationEntryPoint(respuestaError)
						.accessDeniedHandler(respuestaError));
		return http.build();
	}

	private JwtAuthenticationConverter convertidorRoles() {
		var roles = new JwtGrantedAuthoritiesConverter();
		roles.setAuthoritiesClaimName(ClaimsToken.ROL);
		roles.setAuthorityPrefix("ROLE_");
		var convertidor = new JwtAuthenticationConverter();
		convertidor.setJwtGrantedAuthoritiesConverter(roles);
		return convertidor;
	}

}