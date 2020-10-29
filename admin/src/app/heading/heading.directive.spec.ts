import { Component } from '@angular/core';
import { ComponentTester } from 'ngx-speculoos';
import { TestBed } from '@angular/core/testing';
import { HeadingDirective } from './heading.directive';

@Component({
  template: `
    <biom-heading level="1" title="heading 1"></biom-heading>
    <biom-heading level="0" title="heading 0"></biom-heading>
    <biom-heading level="7" title="heading 7"></biom-heading>
    <biom-heading [level]="level" title="heading 8"></biom-heading>
  `
})
class TestComponent {
  level = 1;
}

class TestComponentTester extends ComponentTester<TestComponent> {
  constructor() {
    super(TestComponent);
  }
}

describe('heading directive', () => {
  let tester: TestComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestComponent, HeadingDirective]
    });

    tester = new TestComponentTester();
    tester.detectChanges();
  });

  it('should generate headings', () => {
    expect(tester.elements('h1').length).toBe(2);
    expect(tester.elements('h1')[0]).toHaveText('heading 1');
    expect(tester.elements('h1')[1]).toHaveText('heading 8');
    expect(tester.elements('h6').length).toBe(2);
    expect(tester.elements('h6')[0]).toHaveText('heading 0');
    expect(tester.elements('h6')[1]).toHaveText('heading 7');

    tester.componentInstance.level = 4;
    tester.detectChanges();

    expect(tester.elements('h1').length).toBe(1);
    expect(tester.element('h1')).toHaveText('heading 1');
    expect(tester.elements('h4').length).toBe(1);
    expect(tester.element('h4')).toHaveText('heading 8');
  });
});
