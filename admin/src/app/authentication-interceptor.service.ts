import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { first, Observable, switchMap } from 'rxjs';
import { CurrentUserService } from './current-user.service';

@Injectable()
export class AuthenticationInterceptorService implements HttpInterceptor {
  constructor(private currentUserService: CurrentUserService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return this.currentUserService.get().pipe(
      first(),
      switchMap(user => {
        if (user) {
          const clone = req.clone({ setHeaders: { Authorization: `Bearer ${user.token}` } });
          return next.handle(clone);
        } else {
          return next.handle(req);
        }
      })
    );
  }
}
