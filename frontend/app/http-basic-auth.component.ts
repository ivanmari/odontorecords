import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpBasicAuth {
  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': 'Basic ' + btoa('greg:turnquist')
    });
  }

  get(url: string): Observable<any> {
    return this.http.get(url, { headers: this.getHeaders() });
  }

  post(url: string, data: any): Observable<any> {
    return this.http.post(url, data, { headers: this.getHeaders() });
  }

  put(url: string, data: any): Observable<any> {
    return this.http.put(url, data, { headers: this.getHeaders() });
  }
}
