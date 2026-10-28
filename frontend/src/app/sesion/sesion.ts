import { Injectable, computed, signal } from '@angular/core';
import { SesionIniciada } from '../api/respuesta';

const LLAVE = 'uco-test-sesion';

/** La sesion actual. Vive en sessionStorage: dura mientras la pestana este abierta. */
@Injectable({ providedIn: 'root' })
export class Sesion {
  private readonly actual = signal<SesionIniciada | null>(leer());

  readonly datos = this.actual.asReadonly();
  readonly iniciada = computed(() => this.actual() !== null);
  readonly esDocente = computed(() => this.actual()?.rol === 'DOCENTE');

  iniciar(sesion: SesionIniciada): void {
    this.actual.set(sesion);
    try {
      sessionStorage.setItem(LLAVE, JSON.stringify(sesion));
    } catch {
      // Sin almacenamiento la sesion sigue en memoria.
    }
  }

  cerrar(): void {
    this.actual.set(null);
    try {
      sessionStorage.removeItem(LLAVE);
    } catch {
      // Nada que limpiar.
    }
  }

  token(): string | null {
    const sesion = this.actual();
    if (sesion && new Date(sesion.expiraEn).getTime() <= Date.now()) {
      this.cerrar();
      return null;
    }
    return sesion?.token ?? null;
  }
}

function leer(): SesionIniciada | null {
  try {
    const guardada = sessionStorage.getItem(LLAVE);
    return guardada ? (JSON.parse(guardada) as SesionIniciada) : null;
  } catch {
    return null;
  }
}
