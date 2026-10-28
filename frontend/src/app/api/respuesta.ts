/** Formato unico de todas las respuestas de la API. */
export interface Respuesta<T> {
  mensajes: string[];
  datos: T[];
  respuestaExitosa: boolean;
}

export interface SesionIniciada {
  token: string;
  expiraEn: string;
  rol: 'DOCENTE' | 'ESTUDIANTE';
  nombre: string;
  idPersona: string;
}

export interface Grupo {
  id: string;
  nombre: string;
  estado: string;
  fechaInicio: string;
  fechaFin: string;
}

export interface EstudianteGrupo {
  id: string;
  nombre: string;
}

export interface Actividad {
  id: string;
  nombre: string;
  estado: string;
  fechaRegistroDesde: string;
  fechaRegistroHasta: string;
}
