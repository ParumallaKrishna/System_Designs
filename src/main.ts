import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';
import { routes } from './app/app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { importProvidersFrom } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { authInterceptor } from './app/core/services/auth-interceptor';

bootstrapApplication(AppComponent, {
  ...appConfig,   // spread existing config
  providers: [
    ...(appConfig.providers || []), // keep existing providers
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    importProvidersFrom(ReactiveFormsModule)           // add routing
  ]
}).catch(err => console.error(err));
