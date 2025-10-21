import { Component, OnInit } from '@angular/core';
//import * as SockJS from 'sockjs-client';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-driver-dashboard',
  standalone: true,
  templateUrl: './driver-dashboard.html',
  styleUrls: ['./driver-dashboard.css'],
  imports: [CommonModule],
})
export class DriverDashboardComponent implements OnInit {
  private stompClient!: Client;
  driverId: number = 101;
  rideRequest: any;

  ngOnInit(): void {
    this.connectWebSocket();
  }

   connectWebSocket() {
    this.stompClient = new Client({
      brokerURL: 'ws://localhost:8082/ws-driver', // native WS URL
      reconnectDelay: 5000,
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Connected: ', frame);
      this.stompClient.subscribe(`/topic/driver/${this.driverId}`, (message: IMessage) => {
        if (message.body) {
          this.rideRequest = JSON.parse(message.body);
          console.log('📩 New Ride Request:', this.rideRequest);
        }
      });
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ', frame.headers['message'], frame.body);
    };

    this.stompClient.activate();
  }

  acceptRide() {
    console.log("✅ Ride Accepted by Driver", this.rideRequest);
    // TODO: Call backend /driver/accept API
  }
}