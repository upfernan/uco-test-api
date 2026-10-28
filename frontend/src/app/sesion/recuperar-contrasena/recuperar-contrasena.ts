import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Api } from '../../api/api';
import { DisenoSesion } from '../diseno-sesion/diseno-sesion';

type Paso = 'correo' | 'codigo' | 'listo';

/** Dos pasos: pedir un codigo al correo y luego cambiar la contrasena con ese codigo. */
@Component({
  selector: 'app-recuperar-contrasena',
  imports: [FormsModule, RouterLink, DisenoSesion],
  styleUrl: '../formulario-sesion.css',
  template: `
    <app-diseno-sesion titulo="Recuperar contraseña" [subtitulo]="subtitulo()">
      @switch (paso()) {
        @case ('correo') {
          <form (ngSubmit)="pedirCodigo()">
            <label class="campo">
              Correo de tu cuenta
              <input type="email" name="correo" [(ngModel)]="correo" autocomplete="email" required />
            </label>
            @if (error()) {
              <p class="aviso error" role="alert">{{ error() }}</p>
            }
            <button class="boton" type="submit" [disabled]="enviando()">
              {{ enviando() ? 'Enviando…' : 'Enviar código' }}
            </button>
          </form>
        }
        @case ('codigo') {
          <form (ngSubmit)="cambiarClave()">
            @if (aviso()) {
              <p class="aviso exito" role="status">{{ aviso() }}</p>
            }
            <label class="campo">
              Código de 6 dígitos
              <input name="codigo" [(ngModel)]="codigo" inputmode="numeric" maxlength="6"
                autocomplete="one-time-code" required />
            </label>
            <label class="campo">
              Nueva contraseña
              <input type="password" name="claveNueva" [(ngModel)]="claveNueva" autocomplete="new-password" required />
            </label>
            <p class="pista">Mínimo 8 caracteres, con al menos una letra y un número.</p>
            @if (error()) {
              <p class="aviso error" role="alert">{{ error() }}</p>
            }
            <button class="boton" type="submit" [disabled]="enviando()">
              {{ enviando() ? 'Guardando…' : 'Cambiar contraseña' }}
            </button>
            <button class="boton secundario" type="button" (click)="paso.set('correo')">Usar otro correo</button>
          </form>
        }
        @case ('listo') {
          <p class="aviso exito" role="status">{{ aviso() }}</p>
        }
      }
      <p class="enlaces"><a routerLink="/ingresar">Volver a iniciar sesión</a></p>
    </app-diseno-sesion>
  `,
})
export class RecuperarContrasena {
  private readonly api = inject(Api);

  protected correo = '';
  protected codigo = '';
  protected claveNueva = '';
  protected readonly paso = signal<Paso>('correo');
  protected readonly enviando = signal(false);
  protected readonly aviso = signal('');
  protected readonly error = signal('');

  protected subtitulo() {
    switch (this.paso()) {
      case 'codigo':
        return `Escribe el código que enviamos a ${this.correo}.`;
      case 'listo':
        return 'Ya puedes entrar con tu nueva contraseña.';
      default:
        return 'Te enviaremos un código a tu correo.';
    }
  }

  protected async pedirCodigo() {
    this.enviando.set(true);
    this.error.set('');
    const respuesta = await this.api.solicitarRecuperacion(this.correo.trim());
    this.enviando.set(false);
    if (!respuesta.respuestaExitosa) {
      this.error.set(respuesta.mensajes.join(' '));
      return;
    }
    this.aviso.set(respuesta.mensajes.join(' '));
    this.paso.set('codigo');
  }

  protected async cambiarClave() {
    this.enviando.set(true);
    this.error.set('');
    const respuesta = await this.api.restablecerClave(this.correo.trim(), this.codigo.trim(), this.claveNueva);
    this.enviando.set(false);
    if (!respuesta.respuestaExitosa) {
      this.error.set(respuesta.mensajes.join(' '));
      return;
    }
    this.aviso.set(respuesta.mensajes.join(' '));
    this.paso.set('listo');
  }
}
