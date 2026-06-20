import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authReq = req.clone({
      withCredentials: true,
      headers: req.headers
        .set('Content-Type', 'application/json')
        .set('Accept', 'application/json')
    });
    return next.handle(authReq);
  }
}
