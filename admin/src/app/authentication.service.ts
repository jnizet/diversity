import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Credentials {
  login: string;
  password: string;
}

export interface AuthenticatedUser {
  id: number;
  login: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  constructor(private http: HttpClient) {}

  login(credentials: Credentials): Observable<AuthenticatedUser> {
    return this.http.post<AuthenticatedUser>('/api/authentication', credentials);
  }
}
