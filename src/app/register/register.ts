import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule, RouterModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  registrationForm: FormGroup;
  isDriver = false;
  response: any;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.registrationForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
      licenseNumber: [''],
      vehicleNumber: [''],
      vehicleType: ['']
    });
  }

  onRoleChange(event: any) {
    const role = event.target.value;
    this.isDriver = role === 'Driver';
    if (this.isDriver) {
      this.registrationForm.get('licenseNumber')?.setValidators(Validators.required);
      this.registrationForm.get('vehicleType')?.setValidators(Validators.required);
      this.registrationForm.get('vehicleNumber')?.setValidators(Validators.required);
    } else {
      this.registrationForm.get('licenseNumber')?.clearValidators();
      this.registrationForm.get('vehicleType')?.clearValidators();
      this.registrationForm.get('vehicleNumber')?.clearValidators();
    }
    this.registrationForm.get('licenseNumber')?.updateValueAndValidity();
    this.registrationForm.get('vehicleType')?.updateValueAndValidity();
    this.registrationForm.get('vehicleNumber')?.updateValueAndValidity();
  }

  onSubmit() {
    if (this.registrationForm.valid) {
      const apiUrl = 'http://localhost:8081/auth/register'; // Change to your Spring Boot endpoint
      this.http.post<any>(apiUrl, this.registrationForm.value).subscribe(
        res => {
          this.response = res;
         // Role-based redirection
          // if (res.role && res.role.toUpperCase() === 'DRIVER' || res.role.toUpperCase() === 'RIDER') {
          //   this.router.navigate(['/driver-dashboard']);
          // } else {
            this.router.navigate(['/login']);
          //}
        },
        err => {
          this.response = { error: 'Registration failed', details: err };
        }
      );
    }
  }
}
