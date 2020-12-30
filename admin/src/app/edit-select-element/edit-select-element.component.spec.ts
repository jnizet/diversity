import { TestBed } from '@angular/core/testing';

import { EditSelectElementComponent } from './edit-select-element.component';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SelectElement } from '../page.model';
import { ComponentTester } from 'ngx-speculoos';
import { ValdemortModule } from 'ngx-valdemort';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';

@Component({
  template: `
    <form [formGroup]="form">
      <biom-edit-select-element formControlName="selectElement" [submitted]="submitted"></biom-edit-select-element>
    </form>
  `
})
class DummyFormComponent {
  submitted = false;
  selectElement: SelectElement = {
    id: 1,
    type: 'SELECT',
    name: 'title',
    description: 'Home title',
    value: 'onb',
    options: { onb: 'ONB', inpn: 'INPN' }
  };
  form = new FormGroup({
    selectElement: new FormControl(this.selectElement)
  });
}

class DummyFormComponentTester extends ComponentTester<DummyFormComponent> {
  constructor() {
    super(DummyFormComponent);
  }

  get description() {
    return this.element('label');
  }

  get selectSelect() {
    return this.select('select');
  }

  get formValue() {
    return this.componentInstance.form.value as { selectElement: SelectElement };
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }
}

describe('EditSelectElementComponent', () => {
  let tester: DummyFormComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule],
      declarations: [DummyFormComponent, EditSelectElementComponent, ValidationDefaultsComponent]
    });

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new DummyFormComponentTester();
    tester.detectChanges();
  });

  it('should display a select for the select element', () => {
    expect(tester.description).toHaveText('Home title');
    expect(tester.selectSelect).toHaveSelectedLabel('ONB');
  });

  it('should emit the new select element on change', () => {
    expect(tester.formValue.selectElement.value).toBe('onb');
    tester.selectSelect.selectLabel('INPN');
    expect(tester.selectSelect).toHaveSelectedLabel('INPN');
    expect(tester.formValue.selectElement.value).toBe('inpn');
  });
});
