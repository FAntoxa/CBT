import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DiaryComponent } from './components/diary/diary.component';

export const routes: Routes = [
  {
    path: 'login', // URL: /login
    component: LoginComponent,
    title: 'Login'
  },
  {
    path: 'diary', // URL: /diary
    component: DiaryComponent,
    title: 'Дневник настроения'
  },
  { 
    path: '', // Пустой путь → перенаправление
    redirectTo: 'login',
    pathMatch: 'full'
  },
  { 
    path: '**', // Все остальные пути → 404
    redirectTo: 'login'
  }
];
