import { Component, forwardRef, Input } from '@angular/core';
import { TextElement } from '../page.model';
import { ControlValueAccessor, FormBuilder, FormControl, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';

/**
 * Form element allowing to edit a text element of a page.
 */
@Component({
  selector: 'biom-edit-text-element',
  templateUrl: './edit-text-element.component.html',
  styleUrls: ['./edit-text-element.component.scss'],
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => EditTextElementComponent), multi: true }]
})
export class EditTextElementComponent implements ControlValueAccessor {
  editedTextElement: TextElement;
  textControl: FormControl;
  private onChange: (value: TextElement) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private fb: FormBuilder) {
    this.textControl = this.fb.control('', Validators.required);
    this.textControl.valueChanges.subscribe((value: string) => this.onChange({ ...this.editedTextElement, text: value }));
    this.textControl.statusChanges.subscribe(() => this.onTouched());
  }

  @Input()
  set submitted(isSubmitted: boolean) {
    if (isSubmitted) {
      this.textControl.markAsTouched();
    }
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  writeValue(element: TextElement): void {
    this.editedTextElement = element;
    this.textControl.setValue(element.text);
  }
}
