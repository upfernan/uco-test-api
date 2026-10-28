import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Api } from '../../api/api';
import { DisenoSesion } from '../diseno-sesion/diseno-sesion';
import { Sesion } from '../sesion';

@Component({
  selector: 'app-ingresar',
  imports: [FormsModule, RouterLink, DisenoSesion],
  styleUrl: '../formulario-sesion.css',
  template: `
    <app-diseno-sesion titulo="Iniciar sesión" subtitulo="Entra para consultar tus grupos y registrar notas.">
      <form (ngSubmit)="entrar()">
        <label class="campo">
          Correo
          <input type="email" name="correo" [(ngModel)]="correo" autocomplete="username" required />
        </label>
        <label class="campo clave">
          Contraseña
          <input [type]="verClave() ? 'text' : 'password'" name="clave" [(ngModel)]="clave"
            autocomplete="current-password" required />
          <button type="button" class="ver" (click)="verClave.set(!verClave())"
            [attr.aria-pressed]="verClave()">{{ verClave() ? 'Ocultar' : 'Mostrar' }}</button>
        </label>
        <a class="derecha" routerLink="/recuperar-contrasena">¿Olvidaste tu contraseña?</a>

        @if (error()) {
          <p class="aviso error" role="alert">{{ error() }}</p>
        }
        <button class="boton" type="submit" [disabled]="enviando()">
          {{ enviando() ? 'Entrando…' : 'Entrar' }}
        </button>
      </form>

      <p class="enlaces">¿No tienes cuenta? <a routerLink="/crear-cuenta">Crear cuenta</a></p>
    </app-diseno-sesion>
  `,
})
export class Ingresar {
  private readonly api = inject(Api);
  private readonly sesion = inject(Sesion);
  private readonly router = inject(Router);

  protected correo = '';
  protected clave = '';
  protected readonly verClave = signal(false);
  protected readonly enviando = signal(false);
  protected readonly error = signal('');

  protected async entrar() {
    this.enviando.set(true);
    this.error.set('');
    const respuesta = await this.api.iniciarSesion(this.correo.trim(), this.clave);
    this.enviando.set(false);
    if (!respuesta.respuestaExitosa) {
      this.error.set(respuesta.mensajes.join(' '));
      return;
    }
    this.sesion.iniciar(respuesta.datos[0]);
    await this.router.navigateByUrl('/inicio');
  }
}
