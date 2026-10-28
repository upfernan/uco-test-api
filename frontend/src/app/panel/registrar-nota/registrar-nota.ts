import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Api } from '../../api/api';
import { Actividad, EstudianteGrupo, Grupo } from '../../api/respuesta';
import { Sesion } from '../../sesion/sesion';

@Component({
  selector: 'app-registrar-nota',
  imports: [FormsModule],
  styleUrl: '../pagina.css',
  template: `
    <section class="encabezado">
      <h1>Registrar nota</h1>
      <p>Elige el grupo, el estudiante y la actividad. La nota va de 0 a 5 con máximo dos decimales.</p>
    </section>

    <form class="tarjeta formulario" (ngSubmit)="registrar()">
      <label class="campo">
        Grupo
        <select name="grupo" [(ngModel)]="idGrupo" (ngModelChange)="cargarGrupo($event)" required>
          @for (grupo of grupos(); track grupo.id) {
            <option [value]="grupo.id">{{ grupo.nombre }}</option>
          }
        </select>
      </label>

      <div class="fila">
        <label class="campo">
          Estudiante
          <select name="estudiante" [(ngModel)]="idEstudiante" required>
            @for (estudiante of estudiantes(); track estudiante.id) {
              <option [value]="estudiante.id">{{ estudiante.nombre }}</option>
            }
          </select>
        </label>
        <label class="campo">
          Actividad
          <select name="actividad" [(ngModel)]="idActividad" required>
            @for (actividad of actividades(); track actividad.id) {
              <option [value]="actividad.id">
                {{ actividad.nombre }}{{ actividad.estado !== 'ACTIVO' ? ' (inactiva)' : '' }}
              </option>
            }
          </select>
        </label>
      </div>

      <label class="campo nota">
        Nota
        <input type="number" name="valor" [(ngModel)]="valor" min="0" max="5" step="0.01" placeholder="0.00" required />
      </label>

      @if (resultado(); as r) {
        <p class="aviso" [class.exito]="r.exito" [class.error]="!r.exito" role="status">{{ r.mensaje }}</p>
      }

      <button class="boton" type="submit" [disabled]="enviando() || !idGrupo || !idEstudiante || !idActividad">
        {{ enviando() ? 'Registrando…' : 'Registrar nota' }}
      </button>
    </form>
  `,
  styles: `
    .formulario {
      display: flex;
      flex-direction: column;
      gap: 16px;
      max-width: 640px;
    }

    .fila {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 16px;
    }

    .nota {
      max-width: 180px;
    }

    .boton {
      align-self: flex-start;
    }
  `,
})
export class RegistrarNota implements OnInit {
  private readonly api = inject(Api);
  private readonly sesion = inject(Sesion);
  private readonly ruta = inject(ActivatedRoute);

  protected readonly grupos = signal<Grupo[]>([]);
  protected readonly estudiantes = signal<EstudianteGrupo[]>([]);
  protected readonly actividades = signal<Actividad[]>([]);
  protected readonly enviando = signal(false);
  protected readonly resultado = signal<{ exito: boolean; mensaje: string } | null>(null);

  protected idGrupo = '';
  protected idEstudiante = '';
  protected idActividad = '';
  protected valor: number | null = null;

  async ngOnInit() {
    const respuesta = await this.api.consultarGrupos(this.sesion.datos()!.idPersona);
    if (!respuesta.respuestaExitosa) {
      this.resultado.set({ exito: false, mensaje: respuesta.mensajes.join(' ') });
      return;
    }
    this.grupos.set(respuesta.datos);
    const pedido = this.ruta.snapshot.queryParamMap.get('grupo');
    const inicial = respuesta.datos.find((grupo) => grupo.id === pedido) ?? respuesta.datos[0];
    if (inicial) {
      this.idGrupo = inicial.id;
      await this.cargarGrupo(inicial.id);
    }
  }

  protected async cargarGrupo(idGrupo: string) {
    this.resultado.set(null);
    const [estudiantes, actividades] = await Promise.all([
      this.api.consultarEstudiantes(idGrupo),
      this.api.consultarActividades(idGrupo),
    ]);
    this.estudiantes.set(estudiantes.datos);
    this.actividades.set(actividades.datos);
    this.idEstudiante = estudiantes.datos[0]?.id ?? '';
    this.idActividad = actividades.datos.find((a) => a.estado === 'ACTIVO')?.id ?? actividades.datos[0]?.id ?? '';
  }

  protected async registrar() {
    this.enviando.set(true);
    const respuesta = await this.api.registrarNota(this.idGrupo, this.idEstudiante, this.idActividad, Number(this.valor));
    this.enviando.set(false);
    this.resultado.set({ exito: respuesta.respuestaExitosa, mensaje: respuesta.mensajes.join(' ') });
    if (respuesta.respuestaExitosa) {
      this.valor = null;
    }
  }
}
