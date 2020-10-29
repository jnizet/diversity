import { TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { ComponentTester } from 'ngx-speculoos';

import { EditPageElementComponent } from './edit-page-element.component';
import { ImageElement, LinkElement, ListElement, ListUnitElement, PageElement, SectionElement, TextElement } from '../page.model';
import { EditTextElementComponent } from '../edit-text-element/edit-text-element.component';
import { EditLinkElementComponent } from '../edit-link-element/edit-link-element.component';
import { ValdemortModule } from 'ngx-valdemort';
import { EditImageElementComponent } from '../edit-image-element/edit-image-element.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HeadingDirective } from '../heading/heading.directive';

@Component({
  template: `
    <form [formGroup]="form">
      <biom-edit-page-element formControlName="element" [elementModel]="model" level="3"></biom-edit-page-element>
    </form>
  `
})
class DummyFormComponent {
  element = {
    id: 1,
    type: 'TEXT',
    name: 'title',
    description: 'Home title',
    multiLine: false,
    text: 'Welcome'
  } as PageElement;
  model = { id: 1, ...this.element, text: '' } as PageElement;
  form = new FormGroup({
    element: new FormControl(this.element)
  });
}

class DummyFormComponentTester extends ComponentTester<DummyFormComponent> {
  constructor() {
    super(DummyFormComponent);
  }

  get textComponent() {
    return this.debugElement.query(By.directive(EditTextElementComponent));
  }

  get linkComponent() {
    return this.debugElement.query(By.directive(EditLinkElementComponent));
  }

  get linkComponents() {
    return this.debugElement.queryAll(By.directive(EditLinkElementComponent));
  }

  get imageComponent() {
    return this.debugElement.query(By.directive(EditImageElementComponent));
  }

  get listHeading() {
    return this.element('h3');
  }

  get addUnitButton() {
    return this.button('#add-unit');
  }

  get firstRemoveUnitButton() {
    return this.button('.remove-unit');
  }

  get sectionHeading() {
    return this.element('h3');
  }
}

describe('EditPageElementComponent', () => {
  let tester: DummyFormComponentTester;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule, NgbModalModule, FontAwesomeModule, HttpClientTestingModule],
      declarations: [
        DummyFormComponent,
        EditPageElementComponent,
        EditTextElementComponent,
        EditLinkElementComponent,
        EditImageElementComponent,
        HeadingDirective
      ]
    });
    tester = new DummyFormComponentTester();
    tester.detectChanges();
  });

  it('should display an EditTextComponent for a text element', () => {
    expect(tester.textComponent).not.toBeNull();
    const component = tester.textComponent.componentInstance as EditTextElementComponent;
    expect(component.editedTextElement).toBe(tester.componentInstance.element as TextElement);
  });

  it('should display an EditLinkComponent for a link element', () => {
    const link: LinkElement = {
      id: 1,
      type: 'LINK',
      description: 'lien',
      name: 'link',
      text: 'Lien 1',
      href: 'https://lien1.fr'
    };
    const linkModel: LinkElement = {
      ...link,
      text: '',
      href: ''
    };
    tester.componentInstance.form.get('element').setValue(link);
    tester.componentInstance.model = linkModel;
    tester.detectChanges();

    expect(tester.linkComponent).not.toBeNull();
    const component = tester.linkComponent.componentInstance as EditLinkElementComponent;
    expect(component.editedLinkElement).toBe(link);
  });

  it('should display an EditImageElement for an image element', () => {
    const image: ImageElement = {
      id: 1,
      type: 'IMAGE',
      description: 'Image',
      name: 'image',
      alt: 'Image 1',
      imageId: 42,
      multiSize: true,
      document: false
    };
    tester.componentInstance.form.get('element').setValue(image);
    tester.detectChanges();

    expect(tester.imageComponent).not.toBeNull();
    const component = tester.imageComponent.componentInstance as EditImageElementComponent;
    expect(component.editedImageElement).toBe(image);
  });

  it('should display a list for a list element', () => {
    const link1: LinkElement = {
      id: 1,
      type: 'LINK',
      description: 'lien',
      name: 'link',
      text: 'Lien 1',
      href: 'https://lien1.fr'
    };
    const unit1: ListUnitElement = { id: null, type: 'LIST_UNIT', name: '', description: 'Liens', elements: [link1] };
    const link2: LinkElement = {
      id: 2,
      type: 'LINK',
      description: 'lien',
      name: 'link',
      text: 'Lien 2',
      href: 'https://lien2.fr'
    };
    const unit2: ListUnitElement = { id: null, type: 'LIST_UNIT', name: '', description: 'Liens', elements: [link2] };
    const list: ListElement = { id: 3, type: 'LIST', name: 'links', description: 'Liens', elements: [unit1, unit2] };
    const listModel: ListElement = { ...list, elements: [{ ...unit1, elements: [{ ...link1, text: '', href: '' }] }] };
    tester.componentInstance.form.get('element').setValue(list);
    tester.componentInstance.model = listModel;
    tester.detectChanges();

    expect(tester.linkComponents.length).toBe(2);
    expect(tester.listHeading).toHaveText('Liens');
    const component = tester.linkComponent.componentInstance as EditLinkElementComponent;
    expect(component.editedLinkElement).toBe(link1);

    // add a new unit
    tester.addUnitButton.click();
    expect(tester.linkComponents.length).toBe(3); // plus one
    const newComponent = tester.linkComponents[2].componentInstance as EditLinkElementComponent;
    expect(newComponent.editedLinkElement.text).toBe('');
    expect(newComponent.editedLinkElement.href).toBe('');

    // remove the first unit
    tester.firstRemoveUnitButton.click();
    expect(tester.linkComponents.length).toBe(2); // back to 2
    expect(tester.linkComponent.componentInstance.editedLinkElement).toBe(link2);
  });

  it('should display a section for a section element', () => {
    const link1: LinkElement = {
      id: 1,
      type: 'LINK',
      description: 'lien',
      name: 'link',
      text: 'Lien 1',
      href: 'https://lien1.fr'
    };
    const link2: LinkElement = {
      id: 2,
      type: 'LINK',
      description: 'lien',
      name: 'link',
      text: 'Lien 2',
      href: 'https://lien2.fr'
    };
    const section: SectionElement = { id: 3, type: 'SECTION', name: 'links', description: 'Liens', elements: [link1, link2] };
    const sectionModel: SectionElement = { ...section, elements: [{ ...link1 }, { ...link2 }] };
    tester.componentInstance.form.get('element').setValue(section);
    tester.componentInstance.model = sectionModel;
    tester.detectChanges();

    expect(tester.sectionHeading).toHaveText('Liens');
    expect(tester.linkComponents.length).toBe(2);
    const component = tester.linkComponent.componentInstance as EditLinkElementComponent;
    expect(component.editedLinkElement).toBe(link1);
  });
});
