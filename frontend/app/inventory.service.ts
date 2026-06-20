import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DentalSupply } from './inventory';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private baseUrl = 'http://127.0.0.1:8080/inventory';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': 'Basic ' + btoa('ollie:gierke'),
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
  }

  public getAllSupplies(): Observable<DentalSupply[]> {
    return this.http.get<DentalSupply[]>(this.baseUrl, { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  public addOrUpdateSupply(supply: DentalSupply): Observable<any> {
    return this.http.post(this.baseUrl, supply, { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: any) {
    console.error("An error occurred in InventoryService", error);
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : 'Server error';
    return throwError(() => new Error(errMsg));
  }
}
