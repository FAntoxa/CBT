import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';
import { StatisticsComponent } from './components/statistics/statistics.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, StatisticsComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  showStats = false;
  username = '';

  constructor(private authService: AuthService) {
    this.username = this.authService.getCurrentUsername();
  }

  onActivate(component: any) {
    if (component && component.showStats && component.showStats.subscribe) {
      component.showStats.subscribe(() => {
        this.showStats = true;
      });
    }
  }
}