import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { ComponentTester } from 'ngx-speculoos';
import { RouterOutlet } from '@angular/router';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

class AppComponentTester extends ComponentTester<AppComponent> {
  constructor() {
    super(AppComponent);
  }

  get routerOutlet() {
    return this.debugElement.query(By.directive(RouterOutlet));
  }
}

describe('AppComponent', () => {
  let tester: AppComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [RouterTestingModule]
    });

    tester = new AppComponentTester();
    tester.detectChanges();
  });

  it('should have a router outlet', () => {
    expect(tester.routerOutlet).not.toBeNull();
  });
});
