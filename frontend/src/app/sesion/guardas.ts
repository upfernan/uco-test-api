import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { CanMatchFn, Router } from '@angular/router';
import { Sesion } from './sesion';

/** Agrega el token de la sesion a cada peticion a la API. */
export const agregarToken: HttpInterceptorFn = (peticion, siguiente) => {
  const token = inject(Sesion).token();
  return siguiente(token ? peticion.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : peticion);
};

export const requiereSesion: CanMatchFn = () =>
  inject(Sesion).token() !== null || inject(Router).parseUrl('/ingresar');

export const requiereSinSesion: CanMatchFn = () =>
  inject(Sesion).token() === null || inject(Router).parseUrl('/inicio');

export const requiereDocente: CanMatchFn = () =>
  inject(Sesion).esDocente() || inject(Router).parseUrl('/inicio');
