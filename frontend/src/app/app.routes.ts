import { Routes } from '@angular/router';
import { requiereDocente, requiereSesion, requiereSinSesion } from './sesion/guardas';

export const routes: Routes = [
  {
    path: 'ingresar',
    title: 'Ingresar · UCO Test',
    canMatch: [requiereSinSesion],
    loadComponent: () => import('./sesion/ingresar/ingresar').then((m) => m.Ingresar),
  },
  {
    path: 'crear-cuenta',
    title: 'Crear cuenta · UCO Test',
    canMatch: [requiereSinSesion],
    loadComponent: () => import('./sesion/crear-cuenta/crear-cuenta').then((m) => m.CrearCuenta),
  },
  {
    path: 'recuperar-contrasena',
    title: 'Recuperar contraseña · UCO Test',
    loadComponent: () =>
      import('./sesion/recuperar-contrasena/recuperar-contrasena').then((m) => m.RecuperarContrasena),
  },
  {
    path: '',
    canMatch: [requiereSesion],
    loadComponent: () => import('./panel/panel').then((m) => m.Panel),
    children: [
      {
        path: 'inicio',
        title: 'Inicio · UCO Test',
        loadComponent: () => import('./panel/inicio/inicio').then((m) => m.Inicio),
      },
      {
        path: 'grupos',
        title: 'Mis grupos · UCO Test',
        canMatch: [requiereDocente],
        loadComponent: () => import('./panel/grupos/grupos').then((m) => m.Grupos),
      },
      {
        path: 'registrar-nota',
        title: 'Registrar nota · UCO Test',
        canMatch: [requiereDocente],
        loadComponent: () => import('./panel/registrar-nota/registrar-nota').then((m) => m.RegistrarNota),
      },
      { path: '', pathMatch: 'full', redirectTo: 'inicio' },
    ],
  },
  { path: '**', redirectTo: 'ingresar' },
];
