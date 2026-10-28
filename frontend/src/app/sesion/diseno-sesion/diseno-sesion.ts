import { Component, input } from '@angular/core';

/** Marco de las pantallas de entrada: fondo difuso y una tarjeta centrada. */
@Component({
  selector: 'app-diseno-sesion',
  template: `
    <div class="escena">
      <div class="fondo" aria-hidden="true">
        <span class="mancha uno"></span>
        <span class="mancha dos"></span>
        <span class="mancha tres"></span>
      </div>

      <main class="tarjeta">
        <header class="marca">
          <span class="logo" aria-hidden="true">U</span>
          <span class="nombre">UCO <strong>Test</strong></span>
        </header>
        <h1>{{ titulo() }}</h1>
        @if (subtitulo()) {
          <p class="subtitulo">{{ subtitulo() }}</p>
        }
        <ng-content />
      </main>
    </div>
  `,
  styles: `
    .escena {
      position: relative;
      min-height: 100dvh;
      display: grid;
      place-items: center;
      padding: 32px 16px;
      overflow: hidden;
      isolation: isolate;
    }

    .fondo {
      position: absolute;
      inset: 0;
      z-index: -1;
      background: var(--fondo);
      filter: blur(70px) saturate(1.2);
    }

    .mancha {
      position: absolute;
      border-radius: 50%;
      opacity: 0.75;
    }

    .uno {
      width: 46vmax;
      height: 46vmax;
      top: -14vmax;
      left: -10vmax;
      background: #1c7a4d;
    }

    .dos {
      width: 38vmax;
      height: 38vmax;
      bottom: -16vmax;
      right: -8vmax;
      background: #e0a526;
      opacity: 0.55;
    }

    .tres {
      width: 30vmax;
      height: 30vmax;
      top: 40%;
      left: 55%;
      background: #2f9c8f;
      opacity: 0.5;
    }

    @media (prefers-reduced-motion: no-preference) {
      .mancha {
        animation: flotar 18s ease-in-out infinite alternate;
      }

      .dos {
        animation-duration: 22s;
      }

      .tarjeta {
        animation: subir 420ms cubic-bezier(0.2, 0.7, 0.2, 1) both;
      }
    }

    @keyframes flotar {
      to {
        transform: translate(4vmax, 3vmax) scale(1.08);
      }
    }

    @keyframes subir {
      from {
        opacity: 0;
        transform: translateY(14px);
      }
    }

    .tarjeta {
      width: min(100%, 420px);
      display: flex;
      flex-direction: column;
      gap: 18px;
      padding: 36px 32px;
      border-radius: 20px;
      background: var(--superficie-vidrio);
      border: 1px solid rgb(255 255 255 / 0.25);
      box-shadow: var(--sombra);
      backdrop-filter: blur(18px) saturate(1.4);
      -webkit-backdrop-filter: blur(18px) saturate(1.4);
    }

    .marca {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 6px;
    }

    .logo {
      display: grid;
      place-items: center;
      width: 38px;
      height: 38px;
      border-radius: 11px;
      background: var(--principal);
      color: var(--principal-texto);
      font-weight: 800;
      font-size: 1.15rem;
    }

    .nombre {
      font-size: 1.15rem;
      letter-spacing: 0.01em;
    }

    .nombre strong {
      color: var(--principal);
    }

    h1 {
      font-size: 1.6rem;
    }

    .subtitulo {
      margin: -8px 0 0;
      color: var(--texto-suave);
    }

    @media (max-width: 480px) {
      .tarjeta {
        padding: 28px 20px;
      }
    }
  `,
})
export class DisenoSesion {
  readonly titulo = input.required<string>();
  readonly subtitulo = input<string>('');
}
