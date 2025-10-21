// src/app/services/driver.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

export interface DriverProfileDTO {
  driverId: number;
  name: string;
  vehicleType: string;
  rating: number;
  lat: number;
  lng: number;
  distanceKm?: number;
}

@Injectable({ providedIn: 'root' })
export class DriverService {
  private base = `${environment.apiBaseUrl}/drivers`;

  constructor(private http: HttpClient) {}

  getNearbyDrivers(lat: number, lng: number, radiusKm = 5): Observable<DriverProfileDTO[]> {
    const params = new HttpParams()
      .set('lat', lat)
      .set('lng', lng)
      .set('radiusKm', radiusKm);
    return this.http.get<DriverProfileDTO[]>(`${this.base}/nearby`, { params });
  }
}
