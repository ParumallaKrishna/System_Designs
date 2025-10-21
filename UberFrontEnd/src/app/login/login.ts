import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
   templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      this.authService.login(email, password).subscribe({
      next: (res) => {
        console.log("Login success, role:", res.role);

      if (res.role === 'DRIVER') {
        this.router.navigate(['/driver-dashboard']);
      } else if (res.role === 'RIDER') {
        this.router.navigate(['/rider-dashboard']);
      }
    },
 error: (err) => {
        console.error("Login error", err);
        this.errorMessage = err.error?.error || "Invalid email or password!";
      }
  });
    }
  }
}
