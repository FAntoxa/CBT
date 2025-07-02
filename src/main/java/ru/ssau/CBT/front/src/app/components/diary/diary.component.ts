import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { DiaryService, DiaryEntry, CreateDiaryEntry } from '../../services/diary.service';
import { TextAnalysisService, TextAnalysisResult } from '../../services/text-analysis.service';
import { AuthService } from '../../services/auth.service';

interface Mood {
  value: string;
  displayName: string;
}

@Component({
  selector: 'app-diary',
  templateUrl: './diary.component.html',
  styleUrls: ['./diary.component.scss'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class DiaryComponent implements OnInit {
  diaryForm: FormGroup;
  currentEntry: DiaryEntry | null = null;
  moods: Mood[] = [
    { value: 'JOY', displayName: 'Радость' },
    { value: 'CALM', displayName: 'Спокойствие' },
    { value: 'ANXIETY', displayName: 'Тревога' },
    { value: 'SADNESS', displayName: 'Грусть' },
    { value: 'ANGER', displayName: 'Злость' }
  ];
  currentDate: string;
  username: string;
  loading = false;
  saving = false;
  errorMessage = '';
  textAnalysis: TextAnalysisResult | null = null;
  canEdit = true;
  @Output() showStats = new EventEmitter<void>();
  today = new Date().toISOString().split('T')[0];

  constructor(
    private formBuilder: FormBuilder,
    private diaryService: DiaryService,
    private textAnalysisService: TextAnalysisService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.currentDate = new Date().toISOString().split('T')[0];
    this.username = this.authService.getCurrentUsername();
    this.diaryForm = this.formBuilder.group({
      thought: ['', [Validators.required]],
      mood: ['', [Validators.required]]
    });
    this.diaryForm.get('thought')?.valueChanges.subscribe(text => {
      this.analyzeText(text);
    });
  }

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }
    if (!this.username) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadCurrentEntry();
  }

  loadCurrentEntry(): void {
    this.loading = true;
    this.diaryService.getEntry(this.username, this.currentDate).subscribe({
      next: (entry) => {
        this.currentEntry = entry;
        this.canEdit = false;
        this.diaryForm.patchValue({
          thought: entry.thought,
          mood: entry.mood
        });
        this.analyzeText(entry.thought);
        this.disableForm();
        this.loading = false;
      },
      error: (error) => {
        if (error.status === 404) {
          this.currentEntry = null;
          this.canEdit = true;
          this.diaryForm.reset();
          this.textAnalysis = null;
          this.enableForm();
        } else {
          this.errorMessage = 'Ошибка загрузки записи';
        }
        this.loading = false;
      }
    });
  }

  analyzeText(text: string): void {
    this.textAnalysis = this.textAnalysisService.analyzeText(text);
  }

  onSubmit(): void {
    if (this.diaryForm.valid && this.canEdit) {
      this.saving = true;
      this.errorMessage = '';
      const countnegative = this.textAnalysis?.negativeCount ?? 0;
      const createEntry = {
        username: this.username,
        date: this.currentDate,
        thought: this.diaryForm.value.thought,
        mood: this.diaryForm.value.mood,
        countnegative: countnegative
      };
      this.diaryService.createEntry(createEntry).subscribe({
        next: (entry) => {
          this.currentEntry = entry;
          this.canEdit = false;
          this.errorMessage = '';
          this.disableForm();
        },
        error: () => {
          this.errorMessage = 'Ошибка сохранения записи';
        },
        complete: () => {
          this.saving = false;
        }
      });
    }
  }

  loadPreviousEntry(): void {
    this.loading = true;
    this.errorMessage = '';
    this.diaryService.getPreviousEntry(this.username, this.currentDate).subscribe({
      next: (entry) => {
        this.currentEntry = entry;
        this.currentDate = entry.date;
        this.canEdit = false;
        this.diaryForm.patchValue({
          thought: entry.thought,
          mood: entry.mood
        });
        this.analyzeText(entry.thought);
        this.disableForm();
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;
        if (error.status === 404) {
          this.errorMessage = 'Предыдущая запись не найдена';
        } else {
          this.errorMessage = 'Ошибка загрузки предыдущей записи';
        }
      }
    });
  }

  loadNextEntry(): void {
    const today = this.today;
    if (this.currentDate === today) {
      this.errorMessage = 'Вы уже на сегодняшней дате';
      return;
    }
    this.loading = true;
    this.errorMessage = '';
    this.diaryService.getNextEntry(this.username, this.currentDate).subscribe({
      next: (entry) => {
        this.currentEntry = entry;
        this.currentDate = entry.date;
        this.canEdit = false;
        this.diaryForm.patchValue({
          thought: entry.thought,
          mood: entry.mood
        });
        this.analyzeText(entry.thought);
        this.disableForm();
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;
        if (error.status === 404) {
          const nextDate = this.addDays(this.currentDate, 1);
          if (nextDate > today) {
            this.errorMessage = 'Нельзя перейти на будущую дату';
            return;
          }
          if (nextDate === today) {
            this.currentDate = today;
            this.currentEntry = null;
            this.canEdit = true;
            this.diaryForm.reset();
            this.textAnalysis = null;
            this.enableForm();
          } else {
            this.errorMessage = 'Следующая запись не найдена';
          }
        } else {
          this.errorMessage = 'Ошибка загрузки следующей записи';
        }
      }
    });
  }

  onDateChange(event: any): void {
    const selectedDate = event.target.value;
    if (selectedDate > this.today) {
      this.errorMessage = 'Нельзя выбрать будущую дату';
      return;
    }
    this.currentDate = selectedDate;
    this.loadCurrentEntry();
  }

  addDays(dateStr: string, days: number): string {
    const date = new Date(dateStr);
    date.setDate(date.getDate() + days);
    return date.toISOString().split('T')[0];
  }

  private disableForm(): void {
    this.diaryForm.get('thought')?.disable();
    this.diaryForm.get('mood')?.disable();
  }

  private enableForm(): void {
    this.diaryForm.get('thought')?.enable();
    this.diaryForm.get('mood')?.enable();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onShowStats(): void {
    this.showStats.emit();
  }
} 