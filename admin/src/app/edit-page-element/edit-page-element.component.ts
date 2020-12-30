import { Component, forwardRef, Input } from '@angular/core';
import {
  ContainerElement,
  ImageElement,
  LinkElement,
  ListElement,
  ListUnitElement,
  PageElement,
  SectionElement,
  SelectElement,
  TextElement
} from '../page.model';
import { ControlValueAccessor, FormArray, FormBuilder, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { atLeastOneElement, validElement, validList } from '../validators';

@Component({
  selector: 'biom-edit-page-element',
  templateUrl: './edit-page-element.component.html',
  styleUrls: ['./edit-page-element.component.scss'],
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => EditPageElementComponent), multi: true }]
})
export class EditPageElementComponent implements ControlValueAccessor {
  @Input() elementModel: PageElement;
  @Input() level: number;
  isSubmitted: boolean;
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

  isSelect(element: PageElement): element is SelectElement {
    return element.type === 'SELECT';
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

  @Input()
  set submitted(isSubmitted: boolean) {
    this.isSubmitted = isSubmitted;
    if (isSubmitted) {
      if (this.elementGroup) {
        this.elementGroup.markAsTouched();
      }
      if (this.elementsArray) {
        this.elementsArray.markAsTouched();
      }
    }
  }

  writeValue(element: PageElement): void {
    this.element = element;
    this.elementGroup = this.fb.group({}, [Validators.required, validElement]);
    this.elementGroup.statusChanges.subscribe(() => this.onTouched());

    switch (element.type) {
      case 'TEXT': {
        // the element is a text: we want a simple form with a 'text' control
        const textControl = this.fb.control(element, [Validators.required, validElement]);
        this.elementGroup.addControl('text', textControl);
        textControl.valueChanges.subscribe((value: PageElement) => {
          this.onChange(value);
        });
        break;
      }
      case 'SELECT': {
        // the element is a select: we want a simple form with a 'select' control
        const selectControl = this.fb.control(element, [Validators.required, validElement]);
        this.elementGroup.addControl('select', selectControl);
        selectControl.valueChanges.subscribe((value: PageElement) => {
          this.onChange(value);
        });
        break;
      }
      case 'LINK': {
        // the element is a link: we want a simple form with a 'link' control
        const linkControl = this.fb.control(element, [Validators.required, validElement]);
        this.elementGroup.addControl('link', linkControl);
        linkControl.valueChanges.subscribe((value: PageElement) => {
          this.onChange(value);
        });
        break;
      }
      case 'IMAGE': {
        const imageControl = this.fb.control(element, [Validators.required, validElement]);
        this.elementGroup.addControl('image', imageControl);
        imageControl.valueChanges.subscribe((value: PageElement) => {
          this.onChange(value);
        });
        break;
      }
      case 'SECTION':
      case 'LIST_UNIT': {
        // the element is a section or a list unit: we want a form control for each element of the section
        element.elements.forEach(sectionElement => {
          const sectionElementControl = this.fb.control(sectionElement, [Validators.required, validElement]);
          this.elementGroup.addControl(sectionElement.name, sectionElementControl);
        });
        this.elementGroup.valueChanges.subscribe((value: PageElement) => {
          this.onChange({ ...element, elements: Object.values(value) });
        });
        break;
      }
      case 'LIST': {
        // the element is a list: we want a form array with a form control for each unit of the list
        const elementsArray = this.fb.array([], [Validators.required, validList, atLeastOneElement]);
        this.elementGroup.addControl('elements', elementsArray);
        element.elements.forEach(listUnit => {
          elementsArray.push(this.fb.control(listUnit, [Validators.required, validElement]));
        });
        elementsArray.valueChanges.subscribe((value: PageElement) => {
          this.onChange({ ...element, elements: Object.values(value) });
        });
        break;
      }
    }
  }

  getElementModel(name: string): PageElement {
    return (this.elementModel as ContainerElement).elements.find(el => el.name === name);
  }

  addListUnit(element: ListElement) {
    // get the pristine list unit from the model
    const listUnitElement = { ...(this.elementModel as ListElement).elements[0] };
    // push it to the list elements
    this.elementsArray.push(this.fb.control(listUnitElement, [Validators.required, validElement]));
    element.elements.push(listUnitElement);
  }

  removeListUnit(element: ListElement, unitIndex: number) {
    element.elements.splice(unitIndex, 1);
    this.elementsArray.removeAt(unitIndex);
  }

  get elementsArray() {
    return this.elementGroup ? (this.elementGroup.get('elements') as FormArray) : null;
  }
}
