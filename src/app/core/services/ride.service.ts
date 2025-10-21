// src/app/services/ride.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { DriverProfileDTO } from './driver.service';

export interface RideQuoteRequest {
  originLat: number; originLng: number;
  destLat: number; destLng: number;
}

export interface RideQuoteResponse {
  distanceKm: number;
  durationMin: number;
  estimatedFare: number;
  availableDrivers: DriverProfileDTO[];
}

export interface RideCreateRequest extends RideQuoteRequest {
  riderId: number;
}

export interface RideCreateResponse {
  rideId: number;
  driver: DriverProfileDTO;
  status: 'CONFIRMED'|'NO_DRIVERS';
  etaMin?: number;
  estimatedFare?: number;
}

@Injectable({ providedIn: 'root' })
export class RideService {
  private base = `${environment.apiBaseUrl}/rides`;
  constructor(private http: HttpClient) {}

  getQuote(req: RideQuoteRequest): Observable<RideQuoteResponse> {
    return this.http.post<RideQuoteResponse>(`${this.base}/quote`, req);
  }

  createRide(req: RideCreateRequest): Observable<RideCreateResponse> {
    return this.http.post<RideCreateResponse>(`${this.base}`, req);
  }
}
