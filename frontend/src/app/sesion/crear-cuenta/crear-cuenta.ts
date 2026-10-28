import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Api } from '../../api/api';
import { DisenoSesion } from '../diseno-sesion/diseno-sesion';

@Component({
  selector: 'app-crear-cuenta',
  imports: [FormsModule, RouterLink, DisenoSesion],
  styleUrl: '../formulario-sesion.css',
  template: `
    <app-diseno-sesion titulo="Crear cuenta" subtitulo="Regístrate como docente o estudiante.">
      @if (creada()) {
        <p class="aviso exito" role="status">{{ mensaje() }}</p>
        <a class="boton" routerLink="/ingresar">Ir a iniciar sesión</a>
      } @else {
        <form (ngSubmit)="crear()">
          <label class="campo">
            Nombre completo
            <input name="nombre" [(ngModel)]="nombre" autocomplete="name" required />
          </label>
          <label class="campo">
            Correo
            <input type="email" name="correo" [(ngModel)]="correo" autocomplete="email" required />
          </label>
          <label class="campo">
            Contraseña
            <input type="password" name="clave" [(ngModel)]="clave" autocomplete="new-password" required />
          </label>
          <p class="pista">Mínimo 8 caracteres, con al menos una letra y un número.</p>
          <label class="campo">
            Soy
            <select name="rol" [(ngModel)]="rol">
              <option value="DOCENTE">Docente</option>
              <option value="ESTUDIANTE">Estudiante</option>
            </select>
          </label>

          @if (mensaje()) {
            <p class="aviso error" role="alert">{{ mensaje() }}</p>
          }
          <button class="boton" type="submit" [disabled]="enviando()">
            {{ enviando() ? 'Creando…' : 'Crear cuenta' }}
          </button>
        </form>
        <p class="enlaces">¿Ya tienes cuenta? <a routerLink="/ingresar">Iniciar sesión</a></p>
      }
    </app-diseno-sesion>
  `,
  styles: `
    a.boton {
      display: block;
      text-align: center;
      text-decoration: none;
    }
  `,
})
export class CrearCuenta {
  private readonly api = inject(Api);

  protected nombre = '';
  protected correo = '';
  protected clave = '';
  protected rol = 'DOCENTE';
  protected readonly enviando = signal(false);
  protected readonly creada = signal(false);
  protected readonly mensaje = signal('');

  protected async crear() {
    this.enviando.set(true);
    const respuesta = await this.api.registrarCuenta(this.nombre.trim(), this.correo.trim(), this.clave, this.rol);
    this.enviando.set(false);
    this.mensaje.set(respuesta.mensajes.join(' '));
    this.creada.set(respuesta.respuestaExitosa);
  }
}
