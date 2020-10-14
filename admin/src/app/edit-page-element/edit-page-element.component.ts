import { Component, forwardRef } from '@angular/core';
import { ImageElement, LinkElement, ListElement, ListUnitElement, PageElement, SectionElement, TextElement } from '../page.model';
import { ControlValueAccessor, FormBuilder, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';

@Component({
  selector: 'biom-edit-page-element',
  templateUrl: './edit-page-element.component.html',
  styleUrls: ['./edit-page-element.component.scss'],
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => EditPageElementComponent), multi: true }]
})
export class EditPageElementComponent implements ControlValueAccessor {
  element: PageElement;
  elementGroup: FormGroup;
  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private fb: FormBuilder) {}

  isText(element: PageElement): element is TextElement {
    return element.type === 'TEXT';
  }

  isLink(element: PageElement): element is LinkElement {
    return element.type === 'LINK';
  }

  isImage(element: PageElement): element is ImageElement {
    return element.type === 'IMAGE';
  }

  isListUnit(element: PageElement): element is ListUnitElement {
    return element.type === 'LIST_UNIT';
  }

  isList(element: PageElement): element is ListElement {
    return element.type === 'LIST';
  }

  isSection(element: PageElement): element is SectionElement {
    return element.type === 'SECTION';
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  writeValue(element: PageElement): void {
    this.element = element;
    this.elementGroup = this.fb.group({});
    this.elementGroup.statusChanges.subscribe(() => this.onTouched());

    switch (element.type) {
      case 'TEXT': {
        // the element is a text: we want a simple form with a 'text' control
        const textControl = this.fb.control(element);
        this.elementGroup.addControl('text', textControl);
        textControl.valueChanges.subscribe((value: PageElement) => {
          this.onChange(value);
        });
        break;
      }
      case 'LINK': {
        // the element is a link: we want a simple form with a 'link' control
        const linkControl = this.fb.control(element, Validators.required);
        this.elementGroup.addControl('link', linkControl);
        linkControl.valueChanges.subscribe((value: PageElement) => {
          this.onChange(value);
        });
        break;
      }
      case 'IMAGE': {
        // TODO
        break;
      }
      case 'SECTION':
      case 'LIST_UNIT': {
        // the element is a section or a list unit: we want a form control for each element of the section
        element.elements.forEach(sectionElement => {
          (this.elementGroup as FormGroup).addControl(sectionElement.name, this.fb.control(sectionElement));
        });
        this.elementGroup.valueChanges.subscribe((value: PageElement) => {
          this.onChange({ ...element, elements: Object.values(value) });
        });
        break;
      }
      case 'LIST': {
        // the element is a list: we want a form array with a form control for each unit of the list
        const elementsArray = this.fb.array([]);
        this.elementGroup.addControl('elements', elementsArray);
        element.elements.forEach(listUnit => {
          elementsArray.push(this.fb.control(listUnit));
        });
        elementsArray.valueChanges.subscribe((value: PageElement) => {
          this.onChange({ ...element, elements: Object.values(value) });
        });
        break;
      }
    }
  }
}
