import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Api } from '../../api/api';
import { Actividad, EstudianteGrupo, Grupo } from '../../api/respuesta';
import { Sesion } from '../../sesion/sesion';

interface DetalleGrupo {
  grupo: Grupo;
  estudiantes: EstudianteGrupo[];
  actividades: Actividad[];
}

@Component({
  selector: 'app-grupos',
  imports: [DatePipe, RouterLink],
  styleUrl: '../pagina.css',
  template: `
    <section class="encabezado">
      <h1>Mis grupos</h1>
      <p>Tus grupos con sus estudiantes y actividades.</p>
    </section>

    @if (error()) {
      <p class="aviso error" role="alert">{{ error() }}</p>
    } @else if (cargando()) {
      <p class="vacio">Cargando grupos…</p>
    } @else if (grupos().length === 0) {
      <p class="vacio">No tienes grupos asignados.</p>
    }

    @for (detalle of grupos(); track detalle.grupo.id) {
      <article class="tarjeta grupo">
        <header>
          <div>
            <h2>{{ detalle.grupo.nombre }}</h2>
            <p class="fechas">
              {{ detalle.grupo.fechaInicio | date: 'mediumDate' }} – {{ detalle.grupo.fechaFin | date: 'mediumDate' }}
            </p>
          </div>
          <span class="estado" [class.inactivo]="detalle.grupo.estado !== 'ACTIVO'">{{ detalle.grupo.estado }}</span>
        </header>

        <div class="columnas">
          <section>
            <h3>Estudiantes ({{ detalle.estudiantes.length }})</h3>
            <ul>
              @for (estudiante of detalle.estudiantes; track estudiante.id) {
                <li>{{ estudiante.nombre }}</li>
              } @empty {
                <li class="vacio">Sin estudiantes inscritos.</li>
              }
            </ul>
          </section>
          <section>
            <h3>Actividades ({{ detalle.actividades.length }})</h3>
            <ul>
              @for (actividad of detalle.actividades; track actividad.id) {
                <li>
                  <span>{{ actividad.nombre }}</span>
                  <span class="estado" [class.inactivo]="actividad.estado !== 'ACTIVO'">{{ actividad.estado }}</span>
                  <small>Registro hasta {{ actividad.fechaRegistroHasta | date: 'mediumDate' }}</small>
                </li>
              } @empty {
                <li class="vacio">Sin actividades.</li>
              }
            </ul>
          </section>
        </div>

        <a class="boton" routerLink="/registrar-nota" [queryParams]="{ grupo: detalle.grupo.id }">Registrar nota en este grupo</a>
      </article>
    }
  `,
  styles: `
    .grupo {
      display: flex;
      flex-direction: column;
      gap: 18px;
      margin-bottom: 18px;
    }

    .grupo header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      gap: 12px;
    }

    .grupo h2 {
      font-size: 1.25rem;
    }

    .fechas {
      margin: 4px 0 0;
      color: var(--texto-suave);
      font-size: 0.9rem;
    }

    .columnas {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 20px;
    }

    h3 {
      font-size: 0.95rem;
      color: var(--texto-suave);
      margin-bottom: 8px;
    }

    ul {
      list-style: none;
      margin: 0;
      padding: 0;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    li {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 6px 10px;
      padding: 10px 12px;
      border-radius: 10px;
      background: var(--fondo);
    }

    li small {
      width: 100%;
      color: var(--texto-suave);
    }

    .boton {
      align-self: flex-start;
      text-decoration: none;
    }
  `,
})
export class Grupos implements OnInit {
  private readonly api = inject(Api);
  private readonly sesion = inject(Sesion);

  protected readonly grupos = signal<DetalleGrupo[]>([]);
  protected readonly cargando = signal(true);
  protected readonly error = signal('');

  async ngOnInit() {
    const respuesta = await this.api.consultarGrupos(this.sesion.datos()!.idPersona);
    if (!respuesta.respuestaExitosa) {
      this.error.set(respuesta.mensajes.join(' '));
      this.cargando.set(false);
      return;
    }
    const detalles = await Promise.all(
      respuesta.datos.map(async (grupo) => {
        const [estudiantes, actividades] = await Promise.all([
          this.api.consultarEstudiantes(grupo.id),
          this.api.consultarActividades(grupo.id),
        ]);
        return { grupo, estudiantes: estudiantes.datos, actividades: actividades.datos };
      }),
    );
    this.grupos.set(detalles);
    this.cargando.set(false);
  }
}
