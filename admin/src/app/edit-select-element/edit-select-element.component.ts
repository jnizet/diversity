import { Component, forwardRef, Input } from '@angular/core';
import { SelectElement } from '../page.model';
import { ControlValueAccessor, FormBuilder, FormControl, NG_VALUE_ACCESSOR } from '@angular/forms';

/**
 * Form element allowing to select a value for a select element of a page.
 */
@Component({
  selector: 'biom-edit-select-element',
  templateUrl: './edit-select-element.component.html',
  styleUrls: ['./edit-select-element.component.scss'],
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => EditSelectElementComponent), multi: true }]
})
export class EditSelectElementComponent implements ControlValueAccessor {
  editedSelectElement: SelectElement;
  get optionValue() {
    return Object.keys(this.editedSelectElement?.options);
  }
  selectControl: FormControl;
  private onChange: (value: SelectElement) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private fb: FormBuilder) {
    this.selectControl = this.fb.control('');
    this.selectControl.valueChanges.subscribe((value: string) => {
      if (this.selectControl.valid) {
        this.onChange({ ...this.editedSelectElement, value });
      } else {
        this.onChange(null);
      }
    });
    this.selectControl.statusChanges.subscribe(() => {
      this.onTouched();
    });
  }

  @Input()
  set submitted(isSubmitted: boolean) {
    if (isSubmitted) {
      this.selectControl.markAsTouched();
    }
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  writeValue(element: SelectElement): void {
    this.editedSelectElement = element;
    this.selectControl.setValue(element.value);
  }
}
