import { TestBed } from '@angular/core/testing';

import { AuthenticatedUser, AuthenticationService, Credentials } from './authentication.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(AuthenticationService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should authenticate', () => {
    let actual: AuthenticatedUser = null;
    const credentials: Credentials = {
      login: 'arnaud',
      password: 'passw0rd'
    };
    service.login(credentials).subscribe(u => (actual = u));

    const expected = {} as AuthenticatedUser;
    const testRequest = http.expectOne({ method: 'POST', url: '/api/authentication' });
    expect(testRequest.request.body).toBe(credentials);
    testRequest.flush(expected);
    expect(actual).toBe(expected);
  });
});
