import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { DiaryService, DiaryEntry } from '../../services/diary.service';
import { NgChartsModule } from 'ng2-charts';

@Component({
  selector: 'app-statistics',
  standalone: true,
  imports: [CommonModule, NgIf, NgChartsModule],
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {
  @Input() username!: string;
  @Output() close = new EventEmitter<void>();

  entries: DiaryEntry[] = [];
  loading = true;
  error = '';

  moodLabels: string[] = [];
  moodData: number[] = [];
  negativeLabels: string[] = [];
  negativeData: number[] = [];

  moodChartData: any;
  negativeChartData: any;

  positiveStreak = 0;

  constructor(private diaryService: DiaryService) {}

  ngOnInit(): void {
    this.diaryService.getAllEntries(this.username).subscribe({
      next: (entries) => {
        this.entries = entries.sort((a, b) => a.date.localeCompare(b.date));
        this.prepareMoodChart();
        this.prepareNegativeChart();
        this.calculatePositiveStreak();
        this.loading = false;
      },
      error: () => {
        this.error = 'Ошибка загрузки статистики';
        this.loading = false;
      }
    });
  }

  prepareMoodChart(): void {
    this.moodLabels = this.entries.map(e => e.date);
    this.moodData = this.entries.map(e => this.moodToNumber(e.mood));
    this.moodChartData = {
      labels: this.moodLabels,
      datasets: [{ data: this.moodData, label: 'Настроение' }]
    };
  }

  prepareNegativeChart(): void {
    this.negativeLabels = this.entries.map(e => e.date);
    this.negativeData = this.entries.map(e => e.countnegative);
    this.negativeChartData = {
      labels: this.negativeLabels,
      datasets: [{ data: this.negativeData, label: 'Негативные мысли' }]
    };
  }

  moodToNumber(mood: string): number {
    switch (mood) {
      case 'JOY': return 2;
      case 'CALM': return 1;
      case 'ANXIETY': return 0;
      case 'SADNESS': return -1;
      case 'ANGER': return -2;
      default: return 0;
    }
  }

  calculatePositiveStreak(): void {
    let streak = 0;
    for (let i = this.entries.length - 1; i >= 0; i--) {
      const mood = this.entries[i].mood;
      if (mood === 'JOY' || mood === 'CALM') {
        streak++;
      } else {
        break;
      }
    }
    this.positiveStreak = streak;
  }

  onClose(): void {
    this.close.emit();
  }
} 