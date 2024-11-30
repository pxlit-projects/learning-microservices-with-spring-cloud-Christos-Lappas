import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LogService {
  private url = "http://localhost:8087/log/api/log";

  constructor(private http: HttpClient) {}

  getLogs() {
    return this.http
      .get(
        `${this.url}`
      )
    .pipe(
      catchError(() => {
        return of(null);
      })
    );
  }

}
