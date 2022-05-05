import { AfterViewInit, Component, forwardRef, Input } from '@angular/core';
import { TextElement } from '../page.model';
import { ControlValueAccessor, FormBuilder, FormControl, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { IdGeneratorService } from '../id-generator.service';

/**
 * Form element allowing to edit a text element of a page.
 */
@Component({
  selector: 'biom-edit-text-element',
  templateUrl: './edit-text-element.component.html',
  styleUrls: ['./edit-text-element.component.scss'],
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => EditTextElementComponent), multi: true }]
})
export class EditTextElementComponent implements ControlValueAccessor, AfterViewInit {
  editedTextElement: TextElement;
  textControl: FormControl;
  optionalTextControl: FormControl;
  private onChange: (value: TextElement) => void = () => {};
  private onTouched: () => void = () => {};
  idSuffix: string;

  constructor(private fb: FormBuilder, idGenerator: IdGeneratorService) {
    this.idSuffix = idGenerator.generateSuffix();
    this.textControl = this.fb.control('');
    this.textControl.valueChanges.subscribe((value: string) => {
      if (this.textControl.valid) {
        this.onChange({ ...this.editedTextElement, text: value });
      } else {
        this.onChange(null);
      }
    });
    this.textControl.statusChanges.subscribe(() => {
      this.onTouched();
    });
  }

  ngAfterViewInit() {
    if (!this.editedTextElement.optional) {
      this.textControl.setValidators([Validators.required]);
    }
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
