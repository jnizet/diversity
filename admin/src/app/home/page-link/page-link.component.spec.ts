import { TestBed } from '@angular/core/testing';

import { PageLinkComponent } from './page-link.component';
import { PageLink } from '../../page.model';
import { Component } from '@angular/core';
import { ComponentTester } from 'ngx-speculoos';
import { RouterTestingModule } from '@angular/router/testing';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  template: '<biom-page-link [pageLink]="pageLink"></biom-page-link>'
})
class TestComponent {
  pageLink: PageLink = {
    id: 1,
    name: 'Home',
    modelName: 'home',
    title: 'Accueil'
  };
}

class TestComponentTester extends ComponentTester<TestComponent> {
  constructor() {
    super(TestComponent);
  }

  get span() {
    return this.element('span');
  }

  get link() {
    return this.element('a');
  }
}

describe('PageLinkComponent', () => {
  let tester: TestComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, FontAwesomeModule],
      declarations: [TestComponent, PageLinkComponent]
    });

    tester = new TestComponentTester();
  });

  it('should display a page link when the link has an existing page', () => {
    tester.detectChanges();

    expect(tester.span).not.toHaveClass('no-page');
    expect(tester.link).toHaveText('Accueil');
    expect(tester.link.attr('href')).toBe('/page-models/home/pages/1/edit');
  });

  it('should display a page link when the link does not have an existing page', () => {
    tester.componentInstance.pageLink.id = null;
    tester.componentInstance.pageLink.title = null;
    tester.detectChanges();

    expect(tester.span).toHaveClass('no-page');
    expect(tester.link).toHaveText('Home');
    expect(tester.link.attr('href')).toBe('/page-models/home/pages/create?name=Home');
  });
});
