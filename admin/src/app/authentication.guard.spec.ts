import { TestBed } from '@angular/core/testing';

import { AuthenticationGuard } from './authentication.guard';
import { RouterTestingModule } from '@angular/router/testing';
import { CurrentUserService } from './current-user.service';
import { of } from 'rxjs';
import { AuthenticatedUser } from './authentication.service';
import { UrlTree } from '@angular/router';
import { createMock } from 'ngx-speculoos';

describe('AuthenticationGuard', () => {
  let guard: AuthenticationGuard;
  let currentUserService: jasmine.SpyObj<CurrentUserService>;

  beforeEach(() => {
    currentUserService = createMock(CurrentUserService);

    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [{ provide: CurrentUserService, useValue: currentUserService }]
    });
    guard = TestBed.inject(AuthenticationGuard);
  });

  it('should return true if there is a current user', (done: DoneFn) => {
    currentUserService.get.and.returnValue(of({} as AuthenticatedUser));
    guard.canActivate().subscribe(result => {
      expect(result).toBe(true);
      done();
    });
  });

  it('should return url tree to login if no current user', (done: DoneFn) => {
    currentUserService.get.and.returnValue(of(null));
    guard.canActivate().subscribe(result => {
      expect(result).toBeInstanceOf(UrlTree);
      expect((result as UrlTree).toString()).toBe('/login');
      done();
    });
  });
});
