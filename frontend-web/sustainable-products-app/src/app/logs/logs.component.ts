import { DatePipe } from '@angular/common';
import { Component, signal } from '@angular/core';
import { Log } from '../models/log';
import { LogService } from '../services/log.service';

@Component({
  selector: 'app-logs',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './logs.component.html',
  styleUrl: './logs.component.css'
})
export class LogsComponent {
  logs = signal<Log[]>([]);

  constructor(private logService: LogService) {}

  ngOnInit(): void {
    this.fetchProducts();
  }

  fetchProducts(): void {
    this.logService.getLogs()
    .subscribe({
      next: (response) => {
        this.logs.set(response ?? [])      
      },
      error: (err) => {
        console.error('Failed to fetch logs', err)
      }
    });
  }

}
