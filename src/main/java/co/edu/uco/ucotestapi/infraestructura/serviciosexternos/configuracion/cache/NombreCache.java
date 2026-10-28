package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache;

/**
 * Nombres de las caches de Redis. Cada una guarda el resultado de una consulta de los adaptadores de repositorio.
 */
public final class NombreCache {
	public static final String GRUPOS_DOCENTE = "gruposDocente";
	public static final String ESTUDIANTES_GRUPO = "estudiantesGrupo";
	public static final String NOTAS_GRUPO = "notasGrupo";

	private NombreCache() {
		super();
	}

}
