import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Sesion } from '../../sesion/sesion';

@Component({
  selector: 'app-inicio',
  imports: [RouterLink],
  styleUrl: '../pagina.css',
  template: `
    <section class="encabezado">
      <h1>Hola, {{ sesion.datos()?.nombre }}</h1>
      <p>{{ sesion.esDocente() ? 'Esto es lo que puedes hacer hoy.' : 'Bienvenido a UCO Test.' }}</p>
    </section>

    @if (sesion.esDocente()) {
      <div class="rejilla">
        <a class="tarjeta accion" routerLink="/grupos">
          <h2>Mis grupos</h2>
          <p>Consulta tus grupos, sus estudiantes y las actividades evaluables.</p>
          <span>Ver grupos →</span>
        </a>
        <a class="tarjeta accion" routerLink="/registrar-nota">
          <h2>Registrar nota</h2>
          <p>Califica a un estudiante en una actividad abierta. Se le avisa por correo.</p>
          <span>Registrar →</span>
        </a>
      </div>
    } @else {
      <div class="tarjeta">
        <h2>Tus notas</h2>
        <p class="vacio">Cuando un docente registre una nota tuya, te llegará un aviso a tu correo.</p>
      </div>
    }
  `,
  styles: `
    .accion {
      display: flex;
      flex-direction: column;
      gap: 8px;
      color: var(--texto);
      font-weight: 400;
      text-decoration: none;
      transition: transform 150ms, box-shadow 150ms, border-color 150ms;
    }

    .accion:hover {
      text-decoration: none;
      transform: translateY(-2px);
      border-color: var(--principal);
      box-shadow: var(--sombra);
    }

    .accion p {
      margin: 0;
      color: var(--texto-suave);
    }

    .accion span {
      margin-top: auto;
      color: var(--principal);
      font-weight: 700;
    }

    .tarjeta h2 {
      font-size: 1.15rem;
      margin-bottom: 6px;
    }
  `,
})
export class Inicio {
  protected readonly sesion = inject(Sesion);
}
