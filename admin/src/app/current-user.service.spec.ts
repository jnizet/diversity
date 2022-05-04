import { TestBed } from '@angular/core/testing';

import { CurrentUserService, USER_KEY } from './current-user.service';
import { WindowService } from './window.service';
import { AuthenticatedUser } from './authentication.service';
import { createMock } from 'ngx-speculoos';

describe('CurrentUserService', () => {
  let service: CurrentUserService;
  const windowService = createMock(WindowService);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [{ provide: WindowService, useValue: windowService }]
    });
  });

  it('should read user on creation', () => {
    windowService.getLocalStorageItem.and.returnValue('{ "login": "nicolas", "token": "jwt" }');
    service = TestBed.inject(CurrentUserService);

    let actualUser: AuthenticatedUser = null;
    service.get().subscribe(user => (actualUser = user));

    expect(windowService.getLocalStorageItem).toHaveBeenCalledWith(USER_KEY);
    expect(actualUser.login).toBe('nicolas');
    expect(actualUser.token).toBe('jwt');
  });

  it('should set a new user', () => {
    windowService.getLocalStorageItem.and.returnValue(null);
    service = TestBed.inject(CurrentUserService);

    let actualUser: AuthenticatedUser = null;
    service.get().subscribe(user => (actualUser = user));
    expect(actualUser).toBeNull();

    // user logs in
    const newUser: AuthenticatedUser = { id: 1, login: 'gregoria', token: 'jwt' };
    service.set(newUser);
    expect(windowService.setLocalStorageItem).toHaveBeenCalledWith(USER_KEY, JSON.stringify(newUser));
    expect(actualUser.login).toBe('gregoria');

    // user logs out
    service.set(null);
    expect(windowService.removeLocalStorageItem).toHaveBeenCalledWith(USER_KEY);
    expect(actualUser).toBeNull();
  });
});
