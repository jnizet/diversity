import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { CurrentUserService } from './current-user.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {
  constructor(private currentUserService: CurrentUserService, private router: Router) {}

  canActivate(): Observable<boolean | UrlTree> {
    return this.currentUserService.get().pipe(map(user => !!user || this.router.parseUrl('/login')));
  }
}
