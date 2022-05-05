import { Component, forwardRef, Input } from '@angular/core';
import { CheckboxElement } from '../page.model';
import { ControlValueAccessor, FormBuilder, FormControl, NG_VALUE_ACCESSOR } from '@angular/forms';
import { IdGeneratorService } from '../id-generator.service';

/**
 * Form element allowing to select a value for a checkbox element of a page.
 */
@Component({
  selector: 'biom-edit-checkbox-element',
  templateUrl: './edit-checkbox-element.component.html',
  styleUrls: ['./edit-checkbox-element.component.scss'],
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => EditCheckboxElementComponent), multi: true }]
})
export class EditCheckboxElementComponent implements ControlValueAccessor {
  editedCheckboxElement: CheckboxElement;

  checkboxControl: FormControl;
  idSuffix: string;
  private onChange: (value: CheckboxElement) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private fb: FormBuilder, idGenerator: IdGeneratorService) {
    this.idSuffix = idGenerator.generateSuffix();
    this.checkboxControl = this.fb.control('');
    this.checkboxControl.valueChanges.subscribe((value: boolean) => {
      if (this.checkboxControl.valid) {
        this.onChange({ ...this.editedCheckboxElement, value });
      } else {
        this.onChange(null);
      }
    });
    this.checkboxControl.statusChanges.subscribe(() => {
      this.onTouched();
    });
  }

  @Input()
  set submitted(isSubmitted: boolean) {
    if (isSubmitted) {
      this.checkboxControl.markAsTouched();
    }
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  writeValue(element: CheckboxElement): void {
    this.editedCheckboxElement = element;
    this.checkboxControl.setValue(element.value);
  }
}
