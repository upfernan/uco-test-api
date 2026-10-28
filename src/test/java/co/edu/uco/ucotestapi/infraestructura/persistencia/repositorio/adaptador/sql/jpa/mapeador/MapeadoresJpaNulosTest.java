package co.edu.uco.ucotestapi.infraestructura.persistencia.repositorio.adaptador.sql.jpa.mapeador;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.edu.uco.ucotestapi.infraestructura.persistencia.mapeador.MapeadorJpa;

/**
 * Cero nulos: si a un mapeador le llega null, devuelve un objeto vacio en vez de lanzar un error.
 */
class MapeadoresJpaNulosTest {

	static Stream<Arguments> mapeadores() {
		var docente = new DocenteMapeadorJpa();
		var estudiante = new EstudianteMapeadorJpa();
		var grupo = new GrupoMapeadorJpa(docente);
		var nota = new NotaMapeadorJpa(grupo);
		var estudianteGrupo = new EstudianteGrupoMapeadorJpa(estudiante, grupo);
		return Stream.of(
				Arguments.of(docente),
				Arguments.of(estudiante),
				Arguments.of(grupo),
				Arguments.of(nota),
				Arguments.of(estudianteGrupo),
				Arguments.of(new NotaEstudianteMapeadorJpa(estudianteGrupo, nota)),
				Arguments.of(new UsuarioMapeadorJpa()));
	}

	@ParameterizedTest
	@MethodSource("mapeadores")
	void conNullDevuelveUnObjetoVacio(final MapeadorJpa<Object, Object> mapeador) {
		var entidad = mapeador.ConvertirAEntidad(null);
		var entidadJpa = mapeador.ConvertirAEntidadJPA(null);

		assertNotNull(entidad);
		assertNotNull(entidadJpa);
		assertNull(invocarGetId(entidad));
		assertNull(invocarGetId(entidadJpa));
	}

	private static Object invocarGetId(final Object objeto) {
		try {
			var clase = objeto.getClass();
			var metodo = clase.isRecord() ? clase.getMethod("id") : clase.getMethod("getId");
			return metodo.invoke(objeto);
		} catch (ReflectiveOperationException excepcion) {
			throw new IllegalStateException(excepcion);
		}
	}

}
