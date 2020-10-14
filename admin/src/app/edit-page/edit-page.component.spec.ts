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

  get link1TextInput() {
    return this.elements('input')[2] as TestInput;
  }

  get link1HrefInput() {
    return this.elements('input')[3] as TestInput;
  }

  get link2TextInput() {
    return this.elements('input')[4] as TestInput;
  }

  get link2HrefInput() {
    return this.elements('input')[5] as TestInput;
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
  let router: Router;
  let toastService: jasmine.SpyObj<ToastService>;
  const titleElement: TextElement = { id: 2, type: 'TEXT', name: 'title', description: 'Title', text: 'Portail', multiLine: false };
  const image1: ImageElement = {
    id: 5,
    type: 'IMAGE',
    name: 'image',
    description: 'Image du carousel',
    imageId: 1,
    alt: 'Image 1',
    multiSize: true
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
    multiSize: true
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
    name: 'home',
    title: 'BIOM',
    description: 'Home page',
    elements: [presentationSection]
  };

  function prepare(route: ActivatedRoute) {
    pageService = jasmine.createSpyObj<PageService>('PageService', ['get', 'update']);
    toastService = jasmine.createSpyObj<ToastService>('ToastService', ['success']);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule, ValdemortModule],
      declarations: [
        EditPageComponent,
        EditPageElementComponent,
        EditTextElementComponent,
        EditLinkElementComponent,
        ValidationDefaultsComponent
      ],
      providers: [
        { provide: PageService, useValue: pageService },
        { provide: ActivatedRoute, useValue: route },
        { provide: ToastService, useValue: toastService }
      ]
    });
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new EditPageComponentTester();
  }

  describe('in update mode', () => {
    beforeEach(() => {
      const route = fakeRoute({
        snapshot: fakeSnapshot({
          params: { pageId: '12' }
        })
      });
      prepare(route);

      pageService.get.and.returnValue(of(page));

      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Modifier une page`);
    });

    it('should display a filled form', () => {
      expect(tester.elements('input').length).toBe(6); // title + 5 elements
      expect(tester.pageTitleInput).toHaveValue('BIOM');
      expect(tester.presentationTitleInput).toHaveValue('Portail');
      expect(tester.link1TextInput).toHaveValue('Lien 1');
      expect(tester.link1HrefInput).toHaveValue('https://lien1.fr');
      expect(tester.link2TextInput).toHaveValue('Lien 2');
      expect(tester.link2HrefInput).toHaveValue('https://lien2.fr');
    });

    it('should display an error if the title is missing', () => {
      tester.pageTitleInput.fillWith('');
      tester.saveButton.click();

      expect(tester.errors.length).toBe(1);
      expect(tester.errors[0]).toHaveText('Le titre est obligatoire');

      expect(pageService.update).not.toHaveBeenCalled();
    });

    it('should update the page', () => {
      tester.pageTitleInput.fillWith('BIOM!');
      tester.presentationTitleInput.fillWith('Portail de la bio-diversité');
      tester.link1TextInput.fillWith('Nouveau lien 1');
      tester.link2HrefInput.fillWith('https://lien2.org');

      pageService.update.and.returnValue(of(undefined));
      tester.saveButton.click();

      const titleCommand: TextCommand = { type: 'TEXT', key: 'presentation.title', text: 'Portail de la bio-diversité' };
      const image1Command: ImageCommand = { type: 'IMAGE', key: 'presentation.slides.0.image', imageId: 1, alt: 'Image 1' };
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
        elements: [titleCommand, image1Command, link1Command, image2Command, link2Command]
      };
      expect(pageService.update).toHaveBeenCalledWith(12, expectedCommand);
      // TODO expect(router.navigate).toHaveBeenCalledWith(['/pages']);
      expect(toastService.success).toHaveBeenCalledWith(`La page a été modifiée`);
    });
  });
});
