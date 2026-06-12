import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class HttpBasicAuth {

  constructor(private http: HttpClient) {}

  private authHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', 'Basic ' + btoa('greg:turnquist'));
  }

  get(url: string) {
    const headers = this.authHeader();
    return this.http.get(url, { headers });
  }

  post(url: string, data: any) {
    const headers = this.authHeader();
    return this.http.post(url, data, { headers });
  }

  put(url: string, data: any) {
    const headers = this.authHeader();
    return this.http.put(url, data, { headers });
  }
}