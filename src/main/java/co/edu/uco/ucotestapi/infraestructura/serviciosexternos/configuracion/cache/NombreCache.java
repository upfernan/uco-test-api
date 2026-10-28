package co.edu.uco.ucotestapi.infraestructura.serviciosexternos.configuracion.cache;

/**
 * Nombres de las caches de Redis: las consultas de los adaptadores de repositorio y las plantillas del catalogo de mensajes.
 */
public final class NombreCache {
	public static final String GRUPOS_DOCENTE = "gruposDocente";
	public static final String ESTUDIANTES_GRUPO = "estudiantesGrupo";
	public static final String NOTAS_GRUPO = "notasGrupo";
	public static final String MENSAJES_CATALOGO = "mensajesCatalogo";

	private NombreCache() {
		super();
	}

}
