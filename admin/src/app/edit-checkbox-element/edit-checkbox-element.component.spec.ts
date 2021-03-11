import { TestBed } from '@angular/core/testing';

import { EditCheckboxElementComponent } from './edit-checkbox-element.component';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CheckboxElement } from '../page.model';
import { ComponentTester } from 'ngx-speculoos';
import { ValdemortModule } from 'ngx-valdemort';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';

@Component({
  template: `
    <form [formGroup]="form">
      <biom-edit-checkbox-element formControlName="checkboxElement" [submitted]="submitted"></biom-edit-checkbox-element>
    </form>
  `
})
class DummyFormComponent {
  submitted = false;
  checkboxElement: CheckboxElement = {
    id: 1,
    type: 'CHECKBOX',
    name: 'shadowed',
    description: "Appliquer un effet d'ombrage sur la photo",
    value: true
  };
  form = new FormGroup({
    checkboxElement: new FormControl(this.checkboxElement)
  });
}

class DummyFormComponentTester extends ComponentTester<DummyFormComponent> {
  constructor() {
    super(DummyFormComponent);
  }

  get description() {
    return this.element('label');
  }

  get inputCheckbox() {
    return this.input('input');
  }

  get formValue() {
    return this.componentInstance.form.value as { checkboxElement: CheckboxElement };
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }
}

describe('EditCheckboxElementComponent', () => {
  let tester: DummyFormComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule],
      declarations: [DummyFormComponent, EditCheckboxElementComponent, ValidationDefaultsComponent]
    });

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new DummyFormComponentTester();
    tester.detectChanges();
  });

  it('should display a checkbox for the checkbox element', () => {
    expect(tester.description).toHaveText("Appliquer un effet d'ombrage sur la photo");
    expect(tester.inputCheckbox).toBeChecked();
  });

  it('should emit the new select element on change', () => {
    expect(tester.formValue.checkboxElement.value).toBe(true);
    tester.inputCheckbox.click();
    expect(tester.inputCheckbox).not.toBeChecked();
    expect(tester.formValue.checkboxElement.value).toBe(false);
  });
});
