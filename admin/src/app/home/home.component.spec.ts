import { TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import { ComponentTester } from 'ngx-speculoos';

class HomeComponentTester extends ComponentTester<HomeComponent> {
  constructor() {
    super(HomeComponent);
  }

  get title() {
    return this.element('h1');
  }
}

describe('HomeComponent', () => {
  let tester: HomeComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HomeComponent]
    });
    tester = new HomeComponentTester();
    tester.detectChanges();
  });

  it('should display home', () => {
    expect(tester.title).toHaveText('Administration du portail');
  });
});
