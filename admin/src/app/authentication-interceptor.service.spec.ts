import { TestBed } from '@angular/core/testing';

import { AuthenticationInterceptorService } from './authentication-interceptor.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { CurrentUserService } from './current-user.service';
import { BehaviorSubject } from 'rxjs';
import { AuthenticatedUser } from './authentication.service';

describe('AuthenticationInterceptorService', () => {
  let http: HttpTestingController;
  let httpClient: HttpClient;
  let currentUserSubject: BehaviorSubject<AuthenticatedUser | null>;

  beforeEach(() => {
    currentUserSubject = new BehaviorSubject<AuthenticatedUser | null>(null);
    const currentUserService = jasmine.createSpyObj<CurrentUserService>('CurrentUserService', ['get']);
    currentUserService.get.and.returnValue(currentUserSubject);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptorService, multi: true },
        { provide: CurrentUserService, useValue: currentUserService }
      ]
    });

    http = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => http.verify());

  it('should send request as is if no current user', () => {
    httpClient.get('/api/indicators').subscribe();

    const testRequest = http.expectOne('/api/indicators');
    expect(testRequest.request.headers.has('Authorization')).toBeFalse();
  });

  it('should send request with token if current user', () => {
    currentUserSubject.next({ token: 'foo' } as AuthenticatedUser);
    httpClient.get('/api/indicators').subscribe();

    const testRequest = http.expectOne('/api/indicators');
    expect(testRequest.request.headers.get('Authorization')).toBe('Bearer foo');
  });

  it('should send request only once', () => {
    currentUserSubject.next({ token: 'foo' } as AuthenticatedUser);
    httpClient.get('/api/indicators').subscribe();

    currentUserSubject.next(null);
    currentUserSubject.next({ token: 'bar' } as AuthenticatedUser);

    http.expectOne('/api/indicators');
    http.verify();
  });
});
