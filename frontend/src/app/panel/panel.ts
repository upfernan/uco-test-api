import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Sesion } from '../sesion/sesion';

/** Marco de las pantallas con sesion: barra superior con el menu segun el rol. */
@Component({
  selector: 'app-panel',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <header class="barra">
      <a class="marca" routerLink="/inicio">
        <span class="logo" aria-hidden="true">U</span>
        <span>UCO <strong>Test</strong></span>
      </a>

      <nav aria-label="Menú principal">
        <a routerLink="/inicio" routerLinkActive="activo">Inicio</a>
        @if (sesion.esDocente()) {
          <a routerLink="/grupos" routerLinkActive="activo">Mis grupos</a>
          <a routerLink="/registrar-nota" routerLinkActive="activo">Registrar nota</a>
        }
      </nav>

      <div class="usuario">
        <span class="nombre">{{ sesion.datos()?.nombre }}</span>
        <span class="rol">{{ sesion.esDocente() ? 'Docente' : 'Estudiante' }}</span>
        <button class="boton secundario" type="button" (click)="salir()">Salir</button>
      </div>
    </header>

    <main class="contenido">
      <router-outlet />
    </main>
  `,
  styles: `
    .barra {
      position: sticky;
      top: 0;
      z-index: 10;
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 12px 28px;
      padding: 12px 24px;
      background: var(--superficie-vidrio);
      border-bottom: 1px solid var(--borde);
      backdrop-filter: blur(14px);
      -webkit-backdrop-filter: blur(14px);
    }

    .marca {
      display: flex;
      align-items: center;
      gap: 10px;
      color: var(--texto);
      font-weight: 400;
      font-size: 1.05rem;
      text-decoration: none;
    }

    .marca strong {
      color: var(--principal);
    }

    .logo {
      display: grid;
      place-items: center;
      width: 32px;
      height: 32px;
      border-radius: 9px;
      background: var(--principal);
      color: var(--principal-texto);
      font-weight: 800;
    }

    nav {
      display: flex;
      gap: 4px;
      flex-wrap: wrap;
    }

    nav a {
      padding: 7px 12px;
      border-radius: 8px;
      color: var(--texto-suave);
      font-weight: 600;
      text-decoration: none;
    }

    nav a:hover {
      background: var(--fondo);
      color: var(--texto);
    }

    nav a.activo {
      background: var(--exito-fondo);
      color: var(--principal);
    }

    .usuario {
      margin-left: auto;
      display: flex;
      align-items: center;
      gap: 10px;
    }

    .nombre {
      font-weight: 600;
    }

    .rol {
      font-size: 0.78rem;
      padding: 2px 9px;
      border-radius: 999px;
      border: 1px solid var(--borde);
      color: var(--texto-suave);
    }

    .usuario .boton {
      padding: 6px 12px;
    }

    .contenido {
      max-width: 980px;
      margin: 0 auto;
      padding: 32px 16px 56px;
    }

    @media (max-width: 640px) {
      .barra {
        padding: 12px 16px;
      }

      .usuario {
        margin-left: 0;
        width: 100%;
      }

      .usuario .boton {
        margin-left: auto;
      }
    }
  `,
})
export class Panel {
  protected readonly sesion = inject(Sesion);
  private readonly router = inject(Router);

  protected async salir() {
    this.sesion.cerrar();
    await this.router.navigateByUrl('/ingresar');
  }
}
