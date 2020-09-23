import { TestBed } from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import { ComponentTester } from 'ngx-speculoos';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { RouterTestingModule } from '@angular/router/testing';

class NavbarComponentTester extends ComponentTester<NavbarComponent> {
  constructor() {
    super(NavbarComponent);
  }

  get categories() {
    return this.element('#navbar-indicator-categories');
  }
}

describe('NavbarComponent', () => {
  let tester: NavbarComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NavbarComponent],
      imports: [FontAwesomeModule, RouterTestingModule]
    });
    tester = new NavbarComponentTester();
    tester.detectChanges();
  });

  it('should display elements', () => {
    expect(tester.categories).not.toBeNull();
  });
});
