import { TestBed } from '@angular/core/testing';

import { AuthenticationComponent } from './authentication.component';
import { ComponentTester, createMock } from 'ngx-speculoos';
import { AuthenticatedUser, AuthenticationService } from '../authentication.service';
import { CurrentUserService } from '../current-user.service';
import { ReactiveFormsModule } from '@angular/forms';
import { ValdemortModule } from 'ngx-valdemort';
import { RouterTestingModule } from '@angular/router/testing';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { of } from 'rxjs';

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

  beforeEach(() => {
    authenticationService = createMock(AuthenticationService);
    currentUserService = createMock(CurrentUserService);

    TestBed.configureTestingModule({
      declarations: [AuthenticationComponent, ValidationDefaultsComponent],
      imports: [ReactiveFormsModule, ValdemortModule, RouterTestingModule],
      providers: [
        { provide: AuthenticationService, useValue: authenticationService },
        { provide: CurrentUserService, useValue: currentUserService }
      ]
    });

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new AuthenticationComponentTester();
    tester.detectChanges();
  });

  it('should authenticate and redirect to home', () => {
    tester.login.fillWith('cedric');
    tester.password.fillWith('passw0rd');

    const authenticatedUser = {} as AuthenticatedUser;
    authenticationService.login.and.returnValue(of(authenticatedUser));

    tester.submit.click();

    expect(authenticationService.login).toHaveBeenCalledWith({ login: 'cedric', password: 'passw0rd' });
    expect(currentUserService.set).toHaveBeenCalledWith(authenticatedUser);
  });

  it('should not authenticate if invalid', () => {
    tester.submit.click();

    expect(tester.testElement).toContainText(`L'identifiant est obligatoire`);
    expect(tester.testElement).toContainText(`Le mot de passe est obligatoire`);

    expect(authenticationService.login).not.toHaveBeenCalled();
  });
});
