import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, of } from 'rxjs';
import { Log } from '../models/log';

@Injectable({
  providedIn: 'root'
})
export class LogService {
  private url = "http://localhost:8087/log/api/log";

  constructor(private http: HttpClient) {}

  getLogs() {
    return this.http
      .get<Log[]>(
        `${this.url}`
      )
    .pipe(
      catchError(() => {
        return of(null);
      })
    );
  }

}
