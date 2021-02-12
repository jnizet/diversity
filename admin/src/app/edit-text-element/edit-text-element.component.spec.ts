import { TestBed } from '@angular/core/testing';

import { EditTextElementComponent } from './edit-text-element.component';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TextElement } from '../page.model';
import { ComponentTester } from 'ngx-speculoos';
import { ValdemortModule } from 'ngx-valdemort';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';

@Component({
  template: `
    <form [formGroup]="form">
      <biom-edit-text-element formControlName="textElement" [submitted]="submitted"></biom-edit-text-element>
    </form>
  `
})
class DummyFormComponent {
  submitted = false;
  textElement: TextElement = {
    id: 1,
    type: 'TEXT',
    name: 'title',
    description: 'Home title',
    multiLine: false,
    text: 'Welcome',
    optional: false
  };
  form = new FormGroup({
    textElement: new FormControl(this.textElement)
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
    return this.input('input');
  }

  get textTextarea() {
    return this.textarea('textarea');
  }

  get formValue() {
    return this.componentInstance.form.value as { textElement: TextElement };
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }
}

describe('EditTextElementComponent', () => {
  let tester: DummyFormComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule],
      declarations: [DummyFormComponent, EditTextElementComponent, ValidationDefaultsComponent]
    });

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new DummyFormComponentTester();
    tester.detectChanges();
  });

  it('should display an input for a text element', () => {
    expect(tester.description).toHaveText('Home title');
    expect(tester.textInput).toHaveValue('Welcome');
    expect(tester.textTextarea).toBeNull();
  });

  it('should emit the new text element on change', () => {
    expect(tester.formValue.textElement.text).toBe('Welcome');
    tester.textInput.fillWith('Hello!');
    expect(tester.formValue.textElement.text).toBe('Hello!');
  });

  it('should display an error if the text is missing', () => {
    tester.textInput.fillWith('');
    tester.componentInstance.submitted = true;
    tester.detectChanges();

    expect(tester.errors.length).toBe(1);
    expect(tester.errors[0]).toHaveText('Le texte est obligatoire');
  });

  it('should display a textarea for a multiline text element', () => {
    tester.componentInstance.textElement.multiLine = true;
    tester.detectChanges();

    expect(tester.textInput).toBeNull();
    expect(tester.description).toHaveText('Home title');
    expect(tester.textTextarea).toHaveValue('Welcome');
  });
});
