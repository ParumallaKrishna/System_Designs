import { Routes } from '@angular/router';
import { Welcome } from './welcome/welcome';

 export const routes: Routes = [
  { path: '', component: Welcome },
   { path: 'login', loadComponent: () => import('./login/login').then(m => m.LoginComponent) },
   { path: 'register', loadComponent: () => import('./register/register').then(m => m.RegisterComponent) },
   { path: 'driver-dashboard', loadComponent: () => import('./driver-dashboard/driver-dashboard').then(m => m.DriverDashboardComponent ) },
   { path: 'rider-dashboard', loadComponent: () => import('./rider-dashboard/rider-dashboard').then(m => m.RiderDashboard) }
 ];