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
  private requestedPath?: string;

  constructor(private http: HttpClient) {}

  login(credentials: Credentials): Observable<AuthenticatedUser> {
    return this.http.post<AuthenticatedUser>('/api/authentication', credentials);
  }

  setRequestedPath(path: string) {
    this.requestedPath = path;
  }

  getAndResetRequestedPath(): string | undefined {
    const path = this.requestedPath;
    this.requestedPath = undefined;
    return path;
  }
}
