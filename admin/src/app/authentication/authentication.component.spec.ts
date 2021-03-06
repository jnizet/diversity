import { TestBed } from '@angular/core/testing';

import { AuthenticationComponent } from './authentication.component';
import { ComponentTester, createMock } from 'ngx-speculoos';
import { AuthenticatedUser, AuthenticationService } from '../authentication.service';
import { CurrentUserService } from '../current-user.service';
import { ReactiveFormsModule } from '@angular/forms';
import { ValdemortModule } from 'ngx-valdemort';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { of } from 'rxjs';
import { Router } from '@angular/router';

class AuthenticationComponentTester extends ComponentTester<AuthenticationComponent> {
  constructor() {
    super(AuthenticationComponent);
  }

  get login() {
    return this.input('#login');
  }

  get password() {
    return this.input('#password');
  }

  get submit() {
    return this.button('button');
  }
}

describe('AuthenticationComponent', () => {
  let tester: AuthenticationComponentTester;
  let authenticationService: jasmine.SpyObj<AuthenticationService>;
  let currentUserService: jasmine.SpyObj<CurrentUserService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(() => {
    authenticationService = createMock(AuthenticationService);
    currentUserService = createMock(CurrentUserService);
    router = createMock(Router);

    TestBed.configureTestingModule({
      declarations: [AuthenticationComponent, ValidationDefaultsComponent],
      imports: [ReactiveFormsModule, ValdemortModule],
      providers: [
        { provide: AuthenticationService, useValue: authenticationService },
        { provide: CurrentUserService, useValue: currentUserService },
        { provide: Router, useValue: router }
      ]
    });

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new AuthenticationComponentTester();
    tester.detectChanges();
  });

  it('should authenticate and redirect to home if no requested path set', () => {
    tester.login.fillWith('cedric');
    tester.password.fillWith('passw0rd');

    const authenticatedUser = {} as AuthenticatedUser;
    authenticationService.login.and.returnValue(of(authenticatedUser));

    tester.submit.click();

    expect(authenticationService.login).toHaveBeenCalledWith({ login: 'cedric', password: 'passw0rd' });
    expect(currentUserService.set).toHaveBeenCalledWith(authenticatedUser);
    expect(router.navigateByUrl).toHaveBeenCalledWith('/');
  });

  it('should authenticate and redirect to requested path if set', () => {
    authenticationService.getAndResetRequestedPath.and.returnValue('/requested');
    tester.login.fillWith('cedric');
    tester.password.fillWith('passw0rd');

    const authenticatedUser = {} as AuthenticatedUser;
    authenticationService.login.and.returnValue(of(authenticatedUser));

    tester.submit.click();

    expect(authenticationService.login).toHaveBeenCalledWith({ login: 'cedric', password: 'passw0rd' });
    expect(currentUserService.set).toHaveBeenCalledWith(authenticatedUser);
    expect(router.navigateByUrl).toHaveBeenCalledWith('/requested');
  });

  it('should not authenticate if invalid', () => {
    tester.submit.click();

    expect(tester.testElement).toContainText(`L'identifiant est obligatoire`);
    expect(tester.testElement).toContainText(`Le mot de passe est obligatoire`);

    expect(authenticationService.login).not.toHaveBeenCalled();
  });
});
