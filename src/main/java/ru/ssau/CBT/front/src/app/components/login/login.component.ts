import { Component, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService, LoginRequest } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class LoginComponent {
  @ViewChild('passwordInput') passwordInput!: ElementRef;
  
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      const loginRequest: LoginRequest = {
        username: this.loginForm.value.username,
        password: this.loginForm.value.password
      };

      this.authService.login(loginRequest).subscribe({
        next: (response) => {
          this.loading = false;
          if (response.success) {
            this.authService.setAuthenticated(true, loginRequest.username);
            this.router.navigate(['/diary']);
          } else {
            this.errorMessage = response.message || 'Login failed';
            this.resetPasswordField();
          }
        },
        error: (error) => {
          this.loading = false;
          if (error.status === 401) {
            this.errorMessage = 'Invalid username or password';
            this.resetPasswordField();
          } else {
            this.errorMessage = 'An error occurred during login. Please try again.';
          }
          console.error('Login error:', error);
        }
      });
    }
  }

  private resetPasswordField(): void {
    this.loginForm.patchValue({ password: '' });
    this.loginForm.get('password')?.markAsUntouched();
    // Фокусируемся на поле пароля для удобства повторного ввода
    setTimeout(() => {
      this.passwordInput?.nativeElement?.focus();
    }, 100);
  }

  onInputChange(): void {
    // Очищаем сообщение об ошибке при начале ввода
    if (this.errorMessage) {
      this.errorMessage = '';
    }
  }
} 