import { TestBed } from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import { ComponentTester } from 'ngx-speculoos';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthenticatedUser } from '../authentication.service';
import { Subject } from 'rxjs';
import { CurrentUserService } from '../current-user.service';
import { WindowService } from '../window.service';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbTestingModule } from '../ngb-testing.module';

class NavbarComponentTester extends ComponentTester<NavbarComponent> {
  constructor() {
    super(NavbarComponent);
  }

  get navbar() {
    return this.element('nav');
  }

  get categories() {
    return this.element('#navbar-indicator-categories');
  }

  get logout() {
    return this.element<HTMLAnchorElement>('#nav-logout');
  }
}

describe('NavbarComponent', () => {
  let tester: NavbarComponentTester;
  let userSubject: Subject<AuthenticatedUser>;
  let currentUserService: jasmine.SpyObj<CurrentUserService>;
  let windowService: jasmine.SpyObj<WindowService>;

  beforeEach(() => {
    userSubject = new Subject<AuthenticatedUser>();
    currentUserService = jasmine.createSpyObj<CurrentUserService>('CurrentUserService', ['get', 'set']);
    currentUserService.get.and.returnValue(userSubject);
    windowService = jasmine.createSpyObj<WindowService>('WindowService', ['setLocationHref']);

    TestBed.configureTestingModule({
      declarations: [NavbarComponent],
      imports: [FontAwesomeModule, RouterTestingModule, NgbCollapseModule, NgbTestingModule],
      providers: [
        { provide: CurrentUserService, useValue: currentUserService },
        { provide: WindowService, useValue: windowService }
      ]
    });

    tester = new NavbarComponentTester();
    tester.detectChanges();
  });

  it('should not display anything until there is a current user', () => {
    expect(tester.navbar).toBeNull();

    userSubject.next({ id: 1, login: 'judith', token: 'foo' });
    tester.detectChanges();

    expect(tester.navbar).not.toBeNull();
  });

  describe('once user is present', () => {
    beforeEach(() => {
      userSubject.next({ id: 1, login: 'judith', token: 'foo' });
      tester.detectChanges();
    });

    it('should display elements', () => {
      expect(tester.categories).not.toBeNull();
    });

    it('should display user login', () => {
      expect(tester.testElement).toContainText('judith');
    });

    it('should logout', () => {
      tester.logout.click();
      expect(currentUserService.set).toHaveBeenCalledWith(null);
      expect(windowService.setLocationHref).toHaveBeenCalledWith('/');
    });
  });
});
