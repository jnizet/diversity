import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthenticatedUser } from './authentication.service';
import { WindowService } from './window.service';

const USER_KEY = 'user';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {
  private currentUserSubject = new BehaviorSubject<AuthenticatedUser | null>(null);

  constructor(private windowService: WindowService) {
    const userAsJson = windowService.getLocalStorageItem(USER_KEY);
    if (userAsJson) {
      const user = JSON.parse(userAsJson);
      if (user && user.login && user.token) {
        this.currentUserSubject.next(user);
      }
    }
  }

  get(): Observable<AuthenticatedUser | null> {
    return this.currentUserSubject.asObservable();
  }

  set(newUser: AuthenticatedUser | null) {
    if (!newUser) {
      this.windowService.removeLocalStorageItem(USER_KEY);
    } else {
      this.windowService.setLocalStorageItem(USER_KEY, JSON.stringify(newUser));
    }
    this.currentUserSubject.next(newUser);
  }
}
