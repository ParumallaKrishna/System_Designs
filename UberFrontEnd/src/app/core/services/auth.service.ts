import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

interface LoginResponse {
  role: string;
  riderId?: number;
  driverId?: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8081/auth/login';

  constructor(private http: HttpClient) {}

//   login(email: string, password: string) {
//   return this.http.post<role: string >(
//     this
//     { email, password },
//     { headers: { 'Content-Type': 'application/json' } }
//   );
// }
login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.apiUrl,
      { email, password },
      { headers: { 'Content-Type': 'application/json' } }
    ).pipe(
      tap(res => {
        localStorage.setItem('userRole', res.role);
        if (res.riderId) {
          localStorage.setItem('riderId', res.riderId.toString());
        }
        if (res.driverId) {
          localStorage.setItem('driverId', res.driverId.toString());
        }
      })
    );
  }
getRole(): string | null {
  return localStorage.getItem('userRole');
}

}
