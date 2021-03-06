import { Component, forwardRef, Input } from '@angular/core';
import { LinkElement } from '../page.model';
import { ControlValueAccessor, FormBuilder, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { IdGeneratorService } from '../id-generator.service';

@Component({
  selector: 'biom-edit-link-element',
  templateUrl: './edit-link-element.component.html',
  styleUrls: ['./edit-link-element.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => EditLinkElementComponent),
      multi: true
    }
  ]
})
export class EditLinkElementComponent implements ControlValueAccessor {
  editedLinkElement: LinkElement;
  linkGroup: FormGroup;
  private onChange: (value: LinkElement) => void = () => {};
  private onTouched: () => void = () => {};
  idSuffix: string;

  constructor(private fb: FormBuilder, idGenerator: IdGeneratorService) {
    this.idSuffix = idGenerator.generateSuffix();
    this.linkGroup = this.fb.group({
      text: ['', Validators.required],
      href: ['', Validators.required]
    });
    this.linkGroup.valueChanges.subscribe((value: { text: string; href: string }) => {
      if (this.linkGroup.valid) {
        this.onChange({ ...this.editedLinkElement, ...value });
      } else {
        this.onChange(null);
      }
    });
    this.linkGroup.statusChanges.subscribe(() => this.onTouched());
  }

  @Input()
  set submitted(isSubmitted: boolean) {
    if (isSubmitted) {
      this.linkGroup.markAllAsTouched();
    }
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  writeValue(element: LinkElement): void {
    this.editedLinkElement = element;
    this.linkGroup.setValue(
      {
        text: element.text,
        href: element.href
      },
      { emitEvent: false }
    );
  }
}
