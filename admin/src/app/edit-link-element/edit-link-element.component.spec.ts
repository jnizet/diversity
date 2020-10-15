import { TestBed } from '@angular/core/testing';

import { EditLinkElementComponent } from './edit-link-element.component';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { LinkElement } from '../page.model';
import { ComponentTester } from 'ngx-speculoos';
import { ValdemortModule } from 'ngx-valdemort';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';

@Component({
  template: `
    <form [formGroup]="form">
      <biom-edit-link-element formControlName="linkElement" [submitted]="submitted"></biom-edit-link-element>
    </form>
  `
})
class DummyFormComponent {
  submitted = false;
  linkElement: LinkElement = {
    id: 1,
    type: 'LINK',
    name: 'next',
    description: 'Next indicator link',
    text: 'Surfaces boisées',
    href: '/indicateurs/surfaces-boisees'
  };
  form = new FormGroup({
    linkElement: new FormControl(this.linkElement)
  });
}

class DummyFormComponentTester extends ComponentTester<DummyFormComponent> {
  constructor() {
    super(DummyFormComponent);
  }

  get description() {
    return this.element('label');
  }

  get textInput() {
    return this.input('.link-text');
  }

  get hrefInput() {
    return this.input('.link-href');
  }

  get formValue() {
    return this.componentInstance.form.value as { linkElement: LinkElement };
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }
}

describe('EditLinkElementComponent', () => {
  let tester: DummyFormComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule],
      declarations: [DummyFormComponent, EditLinkElementComponent, ValidationDefaultsComponent]
    });

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new DummyFormComponentTester();
    tester.detectChanges();
  });

  it('should display two inputs for a link element', () => {
    expect(tester.description).toHaveText('Next indicator link');
    expect(tester.textInput).toHaveValue('Surfaces boisées');
    expect(tester.hrefInput).toHaveValue('/indicateurs/surfaces-boisees');
  });

  it('should emit the new link element on change', () => {
    expect(tester.formValue.linkElement.text).toBe('Surfaces boisées');
    expect(tester.formValue.linkElement.href).toBe('/indicateurs/surfaces-boisees');

    tester.textInput.fillWith('Espèces menacées');
    tester.hrefInput.fillWith('/indicateurs/especes-menacees');

    expect(tester.formValue.linkElement.text).toBe('Espèces menacées');
    expect(tester.formValue.linkElement.href).toBe('/indicateurs/especes-menacees');
  });

  it('should display an error if the text or href is missing', () => {
    tester.textInput.fillWith('');
    tester.hrefInput.fillWith('');
    tester.componentInstance.submitted = true;
    tester.detectChanges();

    expect(tester.errors.length).toBe(2);
    expect(tester.errors[0]).toHaveText('Le texte est obligatoire');
    expect(tester.errors[1]).toHaveText('Le lien est obligatoire');
  });
});
