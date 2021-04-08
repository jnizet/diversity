import { TestBed } from '@angular/core/testing';

import { EditImageElementComponent } from './edit-image-element.component';
import { Component } from '@angular/core';
import { ImageElement } from '../page.model';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ComponentTester } from 'ngx-speculoos';
import { ValdemortModule } from 'ngx-valdemort';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { ImageService } from '../image.service';
import { Image } from '../image.model';
import { of, Subject } from 'rxjs';
import { By } from '@angular/platform-browser';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbTestingModule } from '../ngb-testing.module';

@Component({
  template: `
    <form [formGroup]="form">
      <biom-edit-image-element formControlName="imageElement"></biom-edit-image-element>
    </form>
  `
})
class DummyFormComponent {
  imageElement: ImageElement = {
    id: 1,
    type: 'IMAGE',
    name: 'landscape',
    description: 'The landscape picture',
    imageId: 42,
    alt: 'Beautiful landscape by John Doe',
    multiSize: false,
    document: false
  };
  form = new FormGroup({
    imageElement: new FormControl(this.imageElement)
  });
}

class DummyFormComponentTester extends ComponentTester<DummyFormComponent> {
  constructor() {
    super(DummyFormComponent);
  }

  get description() {
    return this.element('label');
  }

  get uploading() {
    return this.element('.uploading');
  }

  get noImage() {
    return this.element('.no-image');
  }

  get imageDiv() {
    return this.element('.image-div');
  }

  get documentDiv() {
    return this.element('.document-div');
  }

  get image() {
    return this.imageDiv.element('img');
  }

  get documentLink() {
    return this.documentDiv.element('a');
  }

  get chooseImageButton() {
    return this.button('.choose-image-button');
  }

  get altInput() {
    return this.input('.image-alt');
  }

  get largeImage(): HTMLImageElement {
    return document.querySelector('.img-fluid');
  }

  get closeDialogButton(): HTMLButtonElement {
    return document.querySelector('button.close');
  }

  get formValue() {
    return this.componentInstance.form.value as { imageElement: ImageElement };
  }

  get editImageComponent(): EditImageElementComponent {
    return this.debugElement.query(By.directive(EditImageElementComponent)).componentInstance;
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }
}

describe('EditImageElementComponent', () => {
  let tester: DummyFormComponentTester;

  let imageService: jasmine.SpyObj<ImageService>;

  beforeEach(() => {
    imageService = jasmine.createSpyObj<ImageService>('ImageService', ['createImage']);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule, NgbModalModule, NgbTestingModule, FontAwesomeModule],
      declarations: [DummyFormComponent, EditImageElementComponent, ValidationDefaultsComponent],
      providers: [{ provide: ImageService, useValue: imageService }]
    });

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new DummyFormComponentTester();
  });

  it('should display an image and an alt input', () => {
    tester.detectChanges();

    expect(tester.description).toHaveText('The landscape picture');
    expect(tester.uploading).toBeNull();
    expect(tester.noImage).toBeNull();
    expect(tester.imageDiv).not.toBeNull();
    expect(tester.image).not.toBeNull();
    expect(tester.image.attr('src')).toBe('/images/42/image');
    expect(tester.chooseImageButton).toHaveText('Choisir une autre image');
    expect(tester.documentDiv).toBeNull();
    expect(tester.altInput).toHaveValue('Beautiful landscape by John Doe');
    expect(tester.largeImage).toBeNull();
  });

  it('should display a document icon and an alt input', () => {
    tester.componentInstance.imageElement.document = true;
    tester.detectChanges();

    expect(tester.description).toHaveText('The landscape picture');
    expect(tester.uploading).toBeNull();
    expect(tester.noImage).toBeNull();
    expect(tester.documentDiv).not.toBeNull();
    expect(tester.documentLink.attr('href')).toBe('/images/42/document');
    expect(tester.imageDiv).toBeNull();
    expect(tester.chooseImageButton).toHaveText('Choisir un autre document');
    expect(tester.altInput).toHaveValue('Beautiful landscape by John Doe');
    expect(tester.largeImage).toBeNull();
  });

  it('should display the small image when multiSize', () => {
    tester.componentInstance.imageElement.multiSize = true;
    tester.detectChanges();

    expect(tester.image.attr('src')).toBe('/images/42/image/sm');
  });

  it('should display no image and different text in choose image button if no image', () => {
    tester.componentInstance.imageElement.imageId = null;
    tester.detectChanges();

    expect(tester.noImage).not.toBeNull();
    expect(tester.noImage).toContainText('Aucune image sélectionnée');
    expect(tester.imageDiv).toBeNull();
    expect(tester.documentDiv).toBeNull();
    expect(tester.chooseImageButton).toHaveText('Choisir une image');
  });

  it('should display no document and different text in choose image button if no document', () => {
    tester.componentInstance.imageElement.imageId = null;
    tester.componentInstance.imageElement.document = true;
    tester.detectChanges();

    expect(tester.noImage).not.toBeNull();
    expect(tester.noImage).toContainText('Aucun document sélectionné');
    expect(tester.imageDiv).toBeNull();
    expect(tester.documentDiv).toBeNull();
    expect(tester.chooseImageButton).toHaveText('Choisir un document');
  });

  it('should display large image in modal', () => {
    tester.detectChanges();
    tester.image.click();

    expect(tester.largeImage).toBeTruthy();
    expect(tester.largeImage.getAttribute('src')).toBe('/images/42/image');
    expect(tester.largeImage.getAttribute('srcset')).toBeFalsy();
    tester.closeDialogButton.click();
  });

  it('should display large image in modal when multiSize image', () => {
    tester.componentInstance.imageElement.multiSize = true;
    tester.detectChanges();
    tester.image.click();

    expect(tester.largeImage).toBeTruthy();
    expect(tester.largeImage.getAttribute('src')).toBe('/images/42/image');
    expect(tester.largeImage.getAttribute('srcset')).toBeTruthy();
    tester.closeDialogButton.click();
  });

  it('should upload image', () => {
    const imageSubject = new Subject<Image>();
    imageService.createImage.and.returnValue(imageSubject);
    tester.detectChanges();

    const file = {} as File;
    const fakeFileEvent = ({
      target: {
        files: [file]
      }
    } as unknown) as Event;

    spyOn(tester.editImageComponent, 'chooseImage').and.callFake(() => tester.editImageComponent.onUpload(fakeFileEvent));
    tester.chooseImageButton.click();
    expect(tester.imageDiv).toBeNull();
    expect(tester.uploading).not.toBeNull();
    expect(tester.chooseImageButton.disabled).toBe(true);

    imageSubject.next({
      id: 54
    } as Image);
    imageSubject.complete();
    tester.detectChanges();

    expect(tester.imageDiv).not.toBeNull();
    expect(tester.uploading).toBeNull();
    expect(tester.image.attr('src')).toBe('/images/54/image');
    expect(tester.chooseImageButton.disabled).toBe(false);
    expect(imageService.createImage).toHaveBeenCalledWith(file, false, false);
  });

  it('should upload a document', () => {
    tester.componentInstance.imageElement.document = true;

    const imageSubject = new Subject<Image>();
    imageService.createImage.and.returnValue(imageSubject);
    tester.detectChanges();

    const file = {} as File;
    const fakeFileEvent = ({
      target: {
        files: [file]
      }
    } as unknown) as Event;

    spyOn(tester.editImageComponent, 'chooseImage').and.callFake(() => tester.editImageComponent.onUpload(fakeFileEvent));
    tester.chooseImageButton.click();
    expect(tester.documentDiv).toBeNull();
    expect(tester.uploading).not.toBeNull();
    expect(tester.chooseImageButton.disabled).toBe(true);

    imageSubject.next({
      id: 54
    } as Image);
    imageSubject.complete();
    tester.detectChanges();

    expect(tester.documentDiv).not.toBeNull();
    expect(tester.uploading).toBeNull();
    expect(tester.chooseImageButton.disabled).toBe(false);
    expect(imageService.createImage).toHaveBeenCalledWith(file, false, true);
  });

  it('should emit the new image element on change', () => {
    tester.detectChanges();
    expect(tester.formValue.imageElement.imageId).toBe(42);
    expect(tester.formValue.imageElement.alt).toBe('Beautiful landscape by John Doe');

    tester.altInput.fillWith('New alt');
    imageService.createImage.and.returnValue(of({ id: 54 } as Image));
    const file = {} as File;
    const fakeFileEvent = ({
      target: {
        files: [file]
      }
    } as unknown) as Event;
    tester.editImageComponent.onUpload(fakeFileEvent);
    tester.detectChanges();

    expect(tester.formValue.imageElement.imageId).toBe(54);
    expect(tester.formValue.imageElement.alt).toBe('New alt');
  });

  it('should display an error if the alt or image is missing', () => {
    tester.componentInstance.imageElement.imageId = null;
    tester.detectChanges();

    tester.altInput.fillWith('');
    tester.element('.image').dispatchEventOfType('submit');

    expect(tester.errors.length).toBe(2);
    expect(tester.errors[0]).toHaveText(`L'image est obligatoire`);
    expect(tester.errors[1]).toHaveText('La légende est obligatoire');
  });
});
