import { Component, forwardRef, Input, OnInit } from '@angular/core';
import {
  ContainerElement,
  ImageElement,
  LinkElement,
  ListElement,
  ListUnitElement,
  PageElement,
  SectionElement,
  SelectElement,
  CheckboxElement,
  TextElement,
  MultiListElement
} from '../page.model';
import { ControlValueAccessor, FormArray, FormBuilder, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { atLeastOneElement, validElement, validList } from '../validators';
import { faAngleUp, faAngleDown, faArrowDown } from '@fortawesome/free-solid-svg-icons';
import { PageService } from '../page.service';

@Component({
  selector: 'biom-edit-page-element',
  templateUrl: './edit-page-element.component.html',
  styleUrls: ['./edit-page-element.component.scss'],
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => EditPageElementComponent), multi: true }]
})
export class EditPageElementComponent implements ControlValueAccessor, OnInit {
  @Input() elementModel: PageElement;
  @Input() level: number;
  @Input() pageName: string;
  @Input() pageModelName: string;
  isSubmitted: boolean;
  element: PageElement;
  elementGroup: FormGroup;

  moveUpItemIcon = faAngleUp;
  moveDownItemIcon = faAngleDown;
  transferData = faArrowDown;

  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private fb: FormBuilder, private pageService: PageService) {}

  ngOnInit() {
    this.elementGroup = this.fb.group({}, { validators: [Validators.required, validElement] });
    this.elementGroup.statusChanges.subscribe(() => this.onTouched());
  }

  isText(element: PageElement): element is TextElement {
    return element.type === 'TEXT';
  }

  isLink(element: PageElement): element is LinkElement {
    return element.type === 'LINK';
  }

  isSelect(element: PageElement): element is SelectElement {
    return element.type === 'SELECT';
  }

  isCheckbox(element: PageElement): element is CheckboxElement {
    return element.type === 'CHECKBOX';
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

  isMultiList(element: PageElement): element is MultiListElement {
    return element.type === 'MULTI_LIST';
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
    this.elementGroup = this.fb.group({}, { validators: [Validators.required, validElement] });
    switch (element.type) {
      case 'TEXT': {
        // the element is a text: we want a simple form with a 'text' control
        const validators = (element as TextElement).optional ? [validElement] : [Validators.required, validElement];
        const textControl = this.fb.control(element, validators);
        this.elementGroup.addControl('text', textControl);
        textControl.valueChanges.subscribe((value: PageElement) => {
          this.onChange(value);
        });
        break;
      }
      case 'SELECT': {
        // the element is a select: we want a simple form with a 'select' control
        const selectControl = this.fb.control(element, [validElement]);
        this.elementGroup.addControl('select', selectControl);
        selectControl.valueChanges.subscribe((value: PageElement) => {
          this.onChange(value);
        });
        break;
      }
      case 'CHECKBOX': {
        // the element is a checkbox: we want a simple form with a 'checkbox' control
        const checkboxControl = this.fb.control(element, [validElement]);
        this.elementGroup.addControl('checkbox', checkboxControl);
        checkboxControl.valueChanges.subscribe((value: PageElement) => {
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
          sectionElement.source = element.source;
          const sectionElementControl = this.fb.control(sectionElement, [Validators.required, validElement]);
          this.elementGroup.addControl(sectionElement.name, sectionElementControl);
        });
        this.elementGroup.valueChanges.subscribe((value: PageElement) => {
          this.onChange({ ...element, elements: Object.values(value) });
        });
        break;
      }
      case 'MULTI_LIST':
      case 'LIST': {
        // the element is a list: we want a form array with a form control for each unit of the list
        const elementsArray = this.fb.array([], [Validators.required, validList, atLeastOneElement]);
        this.elementGroup.addControl('elements', elementsArray);
        element.elements.forEach(listUnit => {
          listUnit.source = element.source;
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
    return (this.elementModel as ContainerElement)?.elements.find(el => el.name === name);
  }

  getMultiListElementModel(name: string): PageElement {
    return (this.elementModel as MultiListElement).templates.find(el => el.name === name);
  }

  addListUnit(element: ListElement) {
    // get the pristine list unit from the model
    const listUnitElement = { ...(this.elementModel as ListElement).elements[0] };
    // push it to the list elements
    this.elementsArray.push(this.fb.control(listUnitElement, [Validators.required, validElement]));
    element.elements.push(listUnitElement);
  }

  addMultiListUnit(element: MultiListElement, template: SectionElement) {
    // push it to the list elements
    element.elements.push(template);
    this.elementsArray.push(this.fb.control(template, [Validators.required, validElement]));
  }

  removeListUnit(element: ListElement | MultiListElement, unitIndex: number) {
    element.elements.splice(unitIndex, 1);
    this.elementsArray.removeAt(unitIndex);
  }

  moveUpListUnit(element: ListElement | MultiListElement, unitIndex: number) {
    [element.elements[unitIndex], element.elements[unitIndex - 1]] = [element.elements[unitIndex - 1], element.elements[unitIndex]];

    const formArrayElement = this.elementsArray.at(unitIndex);
    const formArrayElementToSwapWith = this.elementsArray.at(unitIndex - 1);

    this.elementsArray.removeAt(unitIndex - 1);
    this.elementsArray.removeAt(unitIndex - 1);
    this.elementsArray.insert(unitIndex - 1, formArrayElement);
    this.elementsArray.insert(unitIndex, formArrayElementToSwapWith);
  }

  moveDownListUnit(element: ListElement | MultiListElement, unitIndex: number) {
    [element.elements[unitIndex], element.elements[unitIndex + 1]] = [element.elements[unitIndex + 1], element.elements[unitIndex]];

    const formArrayElement = this.elementsArray.at(unitIndex);
    const formArrayElementToSwapWith = this.elementsArray.at(unitIndex + 1);

    this.elementsArray.removeAt(unitIndex);
    this.elementsArray.removeAt(unitIndex);
    this.elementsArray.insert(unitIndex, formArrayElementToSwapWith);
    this.elementsArray.insert(unitIndex + 1, formArrayElement);
  }

  get elementsArray() {
    return this.elementGroup ? (this.elementGroup.get('elements') as FormArray) : null;
  }

  importSectionData() {
    this.pageService.importPageValue(this.pageName, this.pageModelName).subscribe(page => {
      (page.elements.find(e => e.type === 'SECTION' && e.name === this.element.name) as SectionElement)?.elements.forEach(element => {
        element.source = 'IMPORTED';
        this.elementGroup.controls[element.name].setValue(element);
      });
    });
  }
}
