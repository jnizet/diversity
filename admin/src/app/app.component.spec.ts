import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { ComponentTester } from 'ngx-speculoos';
import { RouterOutlet } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ToastsComponent } from './toasts/toasts.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ValidationDefaultsComponent } from './validation-defaults/validation-defaults.component';
import { ValdemortModule } from 'ngx-valdemort';

class AppComponentTester extends ComponentTester<AppComponent> {
  constructor() {
    super(AppComponent);
  }

  get navbar() {
    return this.element(NavbarComponent);
  }

  get routerOutlet() {
    return this.element(RouterOutlet);
  }

  get toasts() {
    return this.element(ToastsComponent);
  }
}

describe('AppComponent', () => {
  let tester: AppComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppComponent, NavbarComponent, ToastsComponent, ValidationDefaultsComponent],
      imports: [RouterTestingModule, FontAwesomeModule, ValdemortModule]
    });

    tester = new AppComponentTester();
    tester.detectChanges();
  });

  it('should have a navbar', () => {
    expect(tester.navbar).not.toBeNull();
  });

  it('should have a router outlet', () => {
    expect(tester.routerOutlet).not.toBeNull();
  });

  it('should have toasts', () => {
    expect(tester.toasts).not.toBeNull();
  });
});
