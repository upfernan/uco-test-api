import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es-CO';
import { ApplicationConfig, LOCALE_ID, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter, withHashLocation } from '@angular/router';
import { routes } from './app.routes';
import { agregarToken } from './sesion/guardas';

registerLocaleData(localeEs);

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    // Rutas con # para que la API y el gateway sirvan siempre el mismo index.html.
    provideRouter(routes, withHashLocation()),
    provideHttpClient(withInterceptors([agregarToken])),
    { provide: LOCALE_ID, useValue: 'es-CO' },
  ],
};
