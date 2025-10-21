// src/app/rider-dashboard/rider-dashboard.component.ts
import { Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GoogleMapsModule, MapInfoWindow, MapMarker } from '@angular/google-maps';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DriverProfileDTO, DriverService } from '../core/services/driver.service';
import { RideService, RideQuoteResponse, RideCreateResponse } from '../core/services/ride.service';
import { HttpClientModule } from '@angular/common/http';

declare const google: any;

@Component({
  selector: 'app-rider-dashboard',
  standalone: true,
  imports: [CommonModule, GoogleMapsModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './rider-dashboard.html',
  styleUrls: ['./rider-dashboard.css']
})
export class RiderDashboard implements OnInit, OnDestroy {
  private driverSvc = inject(DriverService);
  private rideSvc = inject(RideService);

  // Form
  form = new FormGroup({
    pickup: new FormControl('', [Validators.required]),
    dropoff: new FormControl('', [Validators.required])
  });

  // Refs for Autocomplete inputs
  @ViewChild('pickupInput', { static: true }) pickupInput!: ElementRef<HTMLInputElement>;
  @ViewChild('dropInput', { static: true }) dropInput!: ElementRef<HTMLInputElement>;

  // Map state
  center: google.maps.LatLngLiteral = { lat: 12.9716, lng: 77.5946 }; // default: BLR
  zoom = 13;
  mapOptions: google.maps.MapOptions = { mapTypeControl: false, streetViewControl: false, fullscreenControl: false };
  @ViewChild('map') mapRef!: any;

  pickupMarker?: google.maps.Marker;
  dropMarker?: google.maps.Marker;
  directionsService!: google.maps.DirectionsService;
  directionsRenderer!: google.maps.DirectionsRenderer;

  // Selected places
  pickupCoord?: google.maps.LatLngLiteral;
  dropCoord?: google.maps.LatLngLiteral;

  // Data
  nearbyDrivers: DriverProfileDTO[] = [];
  quote?: RideQuoteResponse;
  confirming = false;
  confirmation?: RideCreateResponse;

  // Responsive panel toggle
  panelOpen = true;

  ngOnInit(): void {
    // Geolocate user
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(pos => {
        this.center = { lat: pos.coords.latitude, lng: pos.coords.longitude };
      });
    }

    // Init Directions
    const init = () => {
      this.directionsService = new google.maps.DirectionsService();
      this.directionsRenderer = new google.maps.DirectionsRenderer({ suppressMarkers: true,
        polylineOptions: {
        strokeColor: '#0000FF',  // ⭐ dark blue
        strokeWeight: 6,         // ⭐ thick line
        strokeOpacity: 0.9       // ⭐ less transparent
      }
       });
      // Will be attached to map after view init
    };

    if ((window as any).google?.maps) init();
    else {
      // in case script not yet ready
      const interval = setInterval(() => {
        if ((window as any).google?.maps) { init(); clearInterval(interval); }
      }, 200);
    }
  }

  ngAfterViewInit() {
    // Attach directions to map when map view ready
    const map = this.mapRef?.googleMap;
    if (map && this.directionsRenderer) this.directionsRenderer.setMap(map);

    // Places Autocomplete
    const pickupAC = new google.maps.places.Autocomplete(this.pickupInput.nativeElement, { fields: ['geometry', 'formatted_address'] });
    pickupAC.addListener('place_changed', () => {
      const place = pickupAC.getPlace();
      const loc = place?.geometry?.location;
      if (loc) {
        this.pickupCoord = { lat: loc.lat(), lng: loc.lng() };
        this.setPickupMarker(this.pickupCoord);
        this.center = this.pickupCoord;
        this.fetchNearbyDrivers();
        this.tryRenderRoute();
      }
    });

    const dropAC = new google.maps.places.Autocomplete(this.dropInput.nativeElement, { fields: ['geometry', 'formatted_address'] });
    dropAC.addListener('place_changed', () => {
      const place = dropAC.getPlace();
      const loc = place?.geometry?.location;
      if (loc) {
        this.dropCoord = { lat: loc.lat(), lng: loc.lng() };
        this.setDropMarker(this.dropCoord);
        this.tryRenderRoute();
      }
    });
  }

  ngOnDestroy(): void {
    // cleanup if needed
  }

  setPickupMarker(coord: google.maps.LatLngLiteral) {
    const map = this.mapRef.googleMap;
    if (!map) return;
    if (!this.pickupMarker) {
      this.pickupMarker = new google.maps.Marker({ map, label: 'P' });
    }
    if (this.pickupMarker) {
    this.pickupMarker.setPosition(coord);
  }
  }

  setDropMarker(coord: google.maps.LatLngLiteral) {
    const map = this.mapRef.googleMap;
    if (!map) return;
    if (!this.dropMarker) {
      this.dropMarker = new google.maps.Marker({ map, label: 'D' });
    }
    if (this.dropMarker) {
    this.dropMarker.setPosition(coord);
    }
  }

  tryRenderRoute() {
    if (!this.pickupCoord || !this.dropCoord || !this.directionsService || !this.directionsRenderer) return;

     const map = this.mapRef?.googleMap;
  if (map && !this.directionsRenderer.getMap()) {
    // attach directionsRenderer to map if not already
    this.directionsRenderer.setMap(map);
  }
    this.directionsService.route(
      {
        origin: this.pickupCoord,
        destination: this.dropCoord,
        travelMode: google.maps.TravelMode.DRIVING
      },
      (res: any, status: any) => {
        if (status === 'OK') {
          this.directionsRenderer.setDirections(res);
          this.getQuoteFromBackend();
        } else {
          console.error('Directions failed:', status);
        }
      }
    );
  }

  fetchNearbyDrivers() {
    if (!this.pickupCoord) return;
    this.driverSvc.getNearbyDrivers(this.pickupCoord.lat, this.pickupCoord.lng, 5).subscribe({
      next: (drivers) => this.nearbyDrivers = drivers,
      error: (e) => console.error(e)
    });
  }

  getQuoteFromBackend() {
    if (!this.pickupCoord || !this.dropCoord) return;
    this.quote = undefined;
    this.rideSvc.getQuote({
      originLat: this.pickupCoord.lat, originLng: this.pickupCoord.lng,
      destLat: this.dropCoord.lat, destLng: this.dropCoord.lng
    }).subscribe({
      next: q => {
        this.quote = q;
        // also show drivers from quote (server can return enriched distances)
        if (q.availableDrivers?.length) this.nearbyDrivers = q.availableDrivers;
      },
      error: err => console.error(err)
    });
  }

  confirmRide() {
    if (!this.pickupCoord || !this.dropCoord) return;
    this.confirming = true;
    const riderId = localStorage.getItem("riderId"); // ✅ get from storage
  if (!riderId) {
    console.error("No riderId found. User not logged in?");
    return;
  }
    // For demo: hardcode riderId = 1001
    this.rideSvc.createRide({
      riderId: +riderId,
      originLat: this.pickupCoord.lat, originLng: this.pickupCoord.lng,
      destLat: this.dropCoord.lat, destLng: this.dropCoord.lng
    }).subscribe({
      next: res => { this.confirmation = res; this.confirming = false; },
      error: err => { console.error(err); this.confirming = false; }
    });
  }

  clearRoute() {
    this.quote = undefined;
    this.confirmation = undefined;
    if (this.directionsRenderer) this.directionsRenderer.set('directions', null);
    this.dropCoord = undefined;
    if (this.dropMarker) this.dropMarker.setMap(null), this.dropMarker = undefined;
    this.form.patchValue({ dropoff: '' });
  }

  togglePanel() { this.panelOpen = !this.panelOpen; }
}
