import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Actividad, EstudianteGrupo, Grupo, Respuesta, SesionIniciada } from './respuesta';

const API = '/api/v1';
const SIN_CONEXION = 'No hay conexión con el servidor. Inténtelo de nuevo.';

/**
 * Habla con la API. Siempre devuelve una Respuesta: si la API responde con error (400, 401, 403...)
 * se usa el cuerpo que ella manda, con los mensajes del catalogo.
 */
@Injectable({ providedIn: 'root' })
export class Api {
  private readonly http = inject(HttpClient);

  iniciarSesion(correo: string, clave: string) {
    return this.enviar<SesionIniciada>('POST', '/auth/login', { correo, clave });
  }

  registrarCuenta(nombre: string, correo: string, clave: string, rol: string) {
    return this.enviar<unknown>('POST', '/auth/registro', { nombre, correo, clave, rol });
  }

  solicitarRecuperacion(correo: string) {
    return this.enviar<unknown>('POST', '/auth/recuperar-clave', { correo });
  }

  restablecerClave(correo: string, codigo: string, claveNueva: string) {
    return this.enviar<unknown>('POST', '/auth/restablecer-clave', { correo, codigo, claveNueva });
  }

  consultarGrupos(idDocente: string) {
    return this.enviar<Grupo>('GET', `/docentes/${idDocente}/grupos`);
  }

  consultarEstudiantes(idGrupo: string) {
    return this.enviar<EstudianteGrupo>('GET', `/grupos/${idGrupo}/estudiantes`);
  }

  consultarActividades(idGrupo: string) {
    return this.enviar<Actividad>('GET', `/grupos/${idGrupo}/notas`);
  }

  registrarNota(idGrupo: string, idEstudiante: string, idNota: string, valor: number) {
    return this.enviar<unknown>('POST', '/notas-estudiantes', { idGrupo, idEstudiante, idNota, valor });
  }

  private async enviar<T>(metodo: 'GET' | 'POST', ruta: string, cuerpo?: unknown): Promise<Respuesta<T>> {
    try {
      return await firstValueFrom(this.http.request<Respuesta<T>>(metodo, API + ruta, { body: cuerpo }));
    } catch (error) {
      if (error instanceof HttpErrorResponse && error.error?.mensajes) {
        return error.error as Respuesta<T>;
      }
      return { mensajes: [SIN_CONEXION], datos: [], respuestaExitosa: false };
    }
  }
}
