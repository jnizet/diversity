import { TestBed } from '@angular/core/testing';

import { EditPageComponent } from './edit-page.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ComponentTester, fakeRoute, fakeSnapshot, TestInput } from 'ngx-speculoos';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../toast.service';
import { PageService } from '../page.service';
import { of } from 'rxjs';
import {
  ImageCommand,
  ImageElement,
  LinkCommand,
  LinkElement,
  ListElement,
  ListUnitElement,
  Page,
  PageCommand,
  SectionElement,
  TextCommand,
  TextElement
} from '../page.model';
import { EditPageElementComponent } from '../edit-page-element/edit-page-element.component';
import { EditTextElementComponent } from '../edit-text-element/edit-text-element.component';
import { EditLinkElementComponent } from '../edit-link-element/edit-link-element.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ValdemortModule } from 'ngx-valdemort';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { EditImageElementComponent } from '../edit-image-element/edit-image-element.component';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Image } from '../image.model';
import { By } from '@angular/platform-browser';
import { ImageService } from '../image.service';
import { HeadingDirective } from '../heading/heading.directive';
import { NgbTestingModule } from '../ngb-testing.module';
import { EditSelectElementComponent } from '../edit-select-element/edit-select-element.component';

class EditPageComponentTester extends ComponentTester<EditPageComponent> {
  constructor() {
    super(EditPageComponent);
  }

  get title() {
    return this.element('h1');
  }

  get pageTitleInput() {
    return this.input('#title');
  }

  get presentationTitleInput() {
    return this.elements('input')[1] as TestInput;
  }

  get image1AltInput() {
    return this.elements('input')[2] as TestInput;
  }

  get link1TextInput() {
    return this.elements('input')[4] as TestInput;
  }

  get link1HrefInput() {
    return this.elements('input')[5] as TestInput;
  }

  get image2AltInput() {
    return this.elements('input')[6] as TestInput;
  }

  get link2TextInput() {
    return this.elements('input')[8] as TestInput;
  }

  get link2HrefInput() {
    return this.elements('input')[9] as TestInput;
  }

  get addUnitButton() {
    return this.button('#add-unit');
  }
  get editImageComponents() {
    return this.debugElement.queryAll(By.directive(EditImageElementComponent));
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }

  get saveButton() {
    return this.button('#save-button');
  }
}

describe('EditPageComponent', () => {
  let tester: EditPageComponentTester;
  let pageService: jasmine.SpyObj<PageService>;
  let imageService: jasmine.SpyObj<ImageService>;
  let router: Router;
  let toastService: jasmine.SpyObj<ToastService>;
  const titleElement: TextElement = {
    id: 2,
    type: 'TEXT',
    name: 'title',
    description: 'Title',
    text: 'Portail',
    multiLine: false,
    optional: false
  };
  const image1: ImageElement = {
    id: 5,
    type: 'IMAGE',
    name: 'image',
    description: 'Image du carousel',
    imageId: 1,
    alt: 'Image 1',
    multiSize: true,
    document: false
  };
  const link1: LinkElement = {
    id: 6,
    type: 'LINK',
    name: 'link',
    description: 'Lien du carousel',
    text: 'Lien 1',
    href: 'https://lien1.fr'
  };
  const slide1: ListUnitElement = {
    id: 4,
    type: 'LIST_UNIT',
    name: '',
    description: '',
    elements: [image1, link1]
  };
  const image2: ImageElement = {
    id: 8,
    type: 'IMAGE',
    name: 'image',
    description: 'Image du carousel',
    imageId: 2,
    alt: 'Image 2',
    multiSize: true,
    document: false
  };
  const link2: LinkElement = {
    id: 9,
    type: 'LINK',
    name: 'link',
    description: 'Lien du carousel',
    text: 'Lien 2',
    href: 'https://lien2.fr'
  };
  const slide2: ListUnitElement = {
    id: 7,
    type: 'LIST_UNIT',
    name: '',
    description: '',
    elements: [image2, link2]
  };
  const carouselSlides: ListElement = {
    id: 3,
    type: 'LIST',
    name: 'slides',
    description: 'Carousel',
    elements: [slide1, slide2]
  };
  const presentationSection: SectionElement = {
    id: 1,
    type: 'SECTION',
    name: 'presentation',
    description: 'Présentation',
    elements: [titleElement, carouselSlides]
  };
  const page: Page = {
    id: 12,
    name: 'Home',
    modelName: 'home',
    title: 'BIOM',
    description: 'Home page',
    elements: [presentationSection]
  };
  const model: Page = {
    id: null,
    name: '',
    modelName: 'home',
    title: '',
    description: 'Home page',
    elements: [
      {
        ...presentationSection,
        elements: [
          { ...titleElement, text: '' },
          {
            ...carouselSlides,
            elements: [
              {
                ...slide1,
                elements: [
                  { ...image1, imageId: null, alt: '' },
                  { ...link1, text: '', href: '' }
                ]
              }
            ]
          }
        ]
      }
    ]
  };

  function prepare(route: ActivatedRoute) {
    pageService = jasmine.createSpyObj<PageService>('PageService', ['getValues', 'getModel', 'update', 'create']);
    imageService = jasmine.createSpyObj<ImageService>('ImageService', ['createImage']);
    toastService = jasmine.createSpyObj<ToastService>('ToastService', ['success']);

    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        ValdemortModule,
        NgbModalModule,
        NgbTestingModule,
        FontAwesomeModule,
        HttpClientTestingModule
      ],
      declarations: [
        EditPageComponent,
        EditPageElementComponent,
        EditTextElementComponent,
        EditSelectElementComponent,
        EditLinkElementComponent,
        EditImageElementComponent,
        ValidationDefaultsComponent,
        HeadingDirective
      ],
      providers: [
        { provide: PageService, useValue: pageService },
        { provide: ActivatedRoute, useValue: route },
        { provide: ToastService, useValue: toastService },
        { provide: ImageService, useValue: imageService }
      ]
    });
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new EditPageComponentTester();
  }

  describe('in create mode', () => {
    beforeEach(() => {
      const route = fakeRoute({
        snapshot: fakeSnapshot({
          params: { modelName: 'home' },
          queryParams: { name: 'accueil' }
        })
      });
      prepare(route);

      pageService.getModel.and.returnValue(of(model));

      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Créer la page accueil`);
    });

    it('should display an empty form', () => {
      expect(tester.elements('input').length).toBe(6); // title + 1 text + 1 link (2 inputs) + 1 image (2 inputs)
      expect(tester.pageTitleInput).toHaveValue('');
      expect(tester.presentationTitleInput).toHaveValue('');
      expect(tester.image1AltInput).toHaveValue('');
      expect(tester.link1TextInput).toHaveValue('');
      expect(tester.link1HrefInput).toHaveValue('');
      expect(tester.image2AltInput).toBeUndefined();
      expect(tester.link2TextInput).toBeUndefined();
      expect(tester.link2HrefInput).toBeUndefined();
    });

    it('should not save if error', () => {
      expect(tester.errors.length).toBe(0);

      tester.saveButton.click();
      expect(tester.errors.length).toBe(6);
      expect(tester.errors[0]).toHaveText('Le titre est obligatoire');
      expect(tester.errors[1]).toHaveText("L'image est obligatoire");
      expect(tester.errors[2]).toHaveText('La légende est obligatoire');
      expect(tester.errors[3]).toHaveText('Le texte est obligatoire');
      expect(tester.errors[4]).toHaveText('Le lien est obligatoire');
      expect(tester.errors[5]).toHaveText('La liste doit contenir des éléments valides');

      expect(pageService.create).not.toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('should create a page', () => {
      tester.pageTitleInput.fillWith('BIOM!');
      tester.presentationTitleInput.fillWith('Portail de la bio-diversité');
      tester.image1AltInput.fillWith('Image 1');
      imageService.createImage.and.returnValue(of({ id: 54 } as Image));
      const file = {} as File;
      const fakeFileEvent = ({
        target: {
          files: [file]
        }
      } as unknown) as Event;
      tester.editImageComponents[0].componentInstance.upload(fakeFileEvent);
      tester.link1TextInput.fillWith('Nouveau lien 1');
      tester.link1HrefInput.fillWith('https://lien1.org');
      // add another unit
      tester.addUnitButton.click();
      expect(tester.elements('input').length).toBe(10); // title + 1 text + 2 links (2 inputs) + 2 images (2 inputs)
      tester.image2AltInput.fillWith('Image 2');
      tester.editImageComponents[1].componentInstance.upload(fakeFileEvent);
      tester.link2TextInput.fillWith('Nouveau lien 2');
      tester.link2HrefInput.fillWith('https://lien2.org');

      pageService.create.and.returnValue(of(page));
      tester.saveButton.click();

      const titleCommand: TextCommand = { type: 'TEXT', key: 'presentation.title', text: 'Portail de la bio-diversité' };
      const image1Command: ImageCommand = { type: 'IMAGE', key: 'presentation.slides.0.image', imageId: 54, alt: 'Image 1' };
      const link1Command: LinkCommand = {
        type: 'LINK',
        key: 'presentation.slides.0.link',
        text: 'Nouveau lien 1',
        href: 'https://lien1.org'
      };
      const image2Command: ImageCommand = { type: 'IMAGE', key: 'presentation.slides.1.image', imageId: 54, alt: 'Image 2' };
      const link2Command: LinkCommand = {
        type: 'LINK',
        key: 'presentation.slides.1.link',
        text: 'Nouveau lien 2',
        href: 'https://lien2.org'
      };
      const expectedCommand: PageCommand = {
        title: 'BIOM!',
        name: 'accueil',
        elements: [titleCommand, image1Command, link1Command, image2Command, link2Command]
      };
      expect(pageService.create).toHaveBeenCalledWith('home', expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/']);
      expect(toastService.success).toHaveBeenCalledWith(`La page a été créée`);
    });
  });

  describe('in update mode', () => {
    beforeEach(() => {
      const route = fakeRoute({
        snapshot: fakeSnapshot({
          params: { pageId: '12' }
        })
      });
      prepare(route);

      pageService.getValues.and.returnValue(of(page));
      pageService.getModel.and.returnValue(of(model));

      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Modifier la page Home`);
    });

    it('should display a filled form', () => {
      expect(tester.elements('input').length).toBe(10); // title + 1 text + 2 links (2 inputs) + 2 images (2 inputs)
      expect(tester.pageTitleInput).toHaveValue('BIOM');
      expect(tester.presentationTitleInput).toHaveValue('Portail');
      expect(tester.image1AltInput).toHaveValue('Image 1');
      expect(tester.link1TextInput).toHaveValue('Lien 1');
      expect(tester.link1HrefInput).toHaveValue('https://lien1.fr');
      expect(tester.image2AltInput).toHaveValue('Image 2');
      expect(tester.link2TextInput).toHaveValue('Lien 2');
      expect(tester.link2HrefInput).toHaveValue('https://lien2.fr');
    });

    it('should display an error if the title is missing', () => {
      tester.pageTitleInput.fillWith('');
      tester.saveButton.click();

      expect(tester.errors.length).toBe(1);
      expect(tester.errors[0]).toHaveText('Le titre est obligatoire');

      expect(pageService.update).not.toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('should update the page', () => {
      tester.pageTitleInput.fillWith('BIOM!');
      tester.presentationTitleInput.fillWith('Portail de la bio-diversité');
      tester.link1TextInput.fillWith('Nouveau lien 1');
      tester.link2HrefInput.fillWith('https://lien2.org');
      tester.image1AltInput.fillWith('New alt 1');

      pageService.update.and.returnValue(of(undefined));
      tester.saveButton.click();

      const titleCommand: TextCommand = { type: 'TEXT', key: 'presentation.title', text: 'Portail de la bio-diversité' };
      const image1Command: ImageCommand = { type: 'IMAGE', key: 'presentation.slides.0.image', imageId: 1, alt: 'New alt 1' };
      const link1Command: LinkCommand = {
        type: 'LINK',
        key: 'presentation.slides.0.link',
        text: 'Nouveau lien 1',
        href: 'https://lien1.fr'
      };
      const image2Command: ImageCommand = { type: 'IMAGE', key: 'presentation.slides.1.image', imageId: 2, alt: 'Image 2' };
      const link2Command: LinkCommand = { type: 'LINK', key: 'presentation.slides.1.link', text: 'Lien 2', href: 'https://lien2.org' };
      const expectedCommand: PageCommand = {
        title: 'BIOM!',
        name: 'Home',
        elements: [titleCommand, image1Command, link1Command, image2Command, link2Command]
      };
      expect(pageService.update).toHaveBeenCalledWith(12, expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/']);
      expect(toastService.success).toHaveBeenCalledWith(`La page a été modifiée`);
    });
  });
});
