import { TestBed } from '@angular/core/testing';

import { ErrorInterceptorService } from './error-interceptor.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { ToastService } from './toast.service';
import { createMock } from 'ngx-speculoos';
import { AuthenticationService } from './authentication.service';
import { Router } from '@angular/router';

describe('ErrorInterceptorService', () => {
  let http: HttpTestingController;
  let httpClient: HttpClient;
  let toastService: jasmine.SpyObj<ToastService>;
  let router: jasmine.SpyObj<Router>;
  let authenticationService: jasmine.SpyObj<AuthenticationService>;

  beforeEach(() => {
    toastService = createMock(ToastService);
    router = createMock(Router);
    authenticationService = createMock(AuthenticationService);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true },
        { provide: ToastService, useValue: toastService },
        { provide: Router, useValue: router },
        { provide: AuthenticationService, useValue: authenticationService }
      ]
    });

    http = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should do nothing if no error', () => {
    httpClient.get('api/foo').subscribe({ error: () => {} });

    http.expectOne('api/foo').flush('test');

    expect(toastService.error).not.toHaveBeenCalled();
  });

  it('should signal functional errors', () => {
    httpClient.get('api/foo').subscribe({ error: () => {} });

    http.expectOne('api/foo').flush({ functionalError: 'FOO', message: 'Ce nom existe déjà' }, { status: 400, statusText: 'Bad Request' });

    expect(toastService.error).toHaveBeenCalledWith('Ce nom existe déjà');
  });

  it('should signal server errors', () => {
    httpClient.get('api/foo').subscribe({ error: () => {} });

    http.expectOne('api/foo').flush({ message: 'FOO' }, { status: 400, statusText: 'Bad Request' });

    expect(toastService.error).toHaveBeenCalledWith(`Une erreur inattendue (400) s'est produite sur le serveur\u00a0: FOO`);
  });

  it('should signal client errors', () => {
    httpClient.get('api/foo').subscribe({ error: () => {} });

    http.expectOne('api/foo').error(new ProgressEvent('error'));

    expect(toastService.error).toHaveBeenCalledWith(`Une erreur inattendue s'est produite à l'envoi d'une requête au serveur`);
  });

  it('should signal 401 errors and redirect to login', () => {
    (router as any).url = '/requested';
    httpClient.get('api/foo').subscribe({ error: () => {} });

    http.expectOne('api/foo').flush('Oops', { status: 401, statusText: 'Unauthorized' });

    expect(toastService.error).toHaveBeenCalledWith(`Vous devez être (ré-)identifié. Redirection vers la page d'identification...`);
    expect(authenticationService.setRequestedPath).toHaveBeenCalledWith('/requested');
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
