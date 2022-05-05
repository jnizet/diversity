import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { ValdemortModule } from 'ngx-valdemort';
import { ComponentTester, createMock, stubRoute } from 'ngx-speculoos';
import { EditMediaCategoryComponent } from './edit-media-category.component';
import { MediaCategoryService } from '../media-category.service';
import { ToastService } from '../toast.service';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { MediaCategory, MediaCategoryCommand } from '../media-category.model';

class EditMediaCategoryComponentTester extends ComponentTester<EditMediaCategoryComponent> {
  constructor() {
    super(EditMediaCategoryComponent);
  }

  get title() {
    return this.element('h1');
  }

  get name() {
    return this.input('#name');
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }

  get saveButton() {
    return this.button('#save-button');
  }
}

describe('EditMediaCategoryComponent', () => {
  let tester: EditMediaCategoryComponentTester;
  let mediaCategoryService: jasmine.SpyObj<MediaCategoryService>;
  let router: Router;
  let toastService: jasmine.SpyObj<ToastService>;

  function prepare(route: ActivatedRoute) {
    mediaCategoryService = createMock(MediaCategoryService);
    toastService = createMock(ToastService);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule, RouterTestingModule],
      declarations: [EditMediaCategoryComponent, ValidationDefaultsComponent],
      providers: [
        { provide: MediaCategoryService, useValue: mediaCategoryService },
        { provide: ActivatedRoute, useValue: route },
        { provide: ToastService, useValue: toastService }
      ]
    });

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new EditMediaCategoryComponentTester();
  }

  describe('in create mode', () => {
    beforeEach(() => {
      const route = stubRoute({
        params: {}
      });
      prepare(route);
      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Créer une catégorie de média`);
    });

    it('should display an empty form', () => {
      expect(tester.name).toHaveValue('');
    });

    it('should not save if error', () => {
      expect(tester.errors.length).toBe(0);

      tester.saveButton.click();

      expect(tester.errors.length).toBe(1);
      expect(tester.errors[0]).toContainText('Le nom est obligatoire');
      expect(mediaCategoryService.create).not.toHaveBeenCalled();
    });

    it('should create a category', () => {
      tester.name.fillWith('Végétation');

      mediaCategoryService.create.and.returnValue(of({} as MediaCategory));
      tester.saveButton.click();

      const expectedCommand: MediaCategoryCommand = {
        name: 'Végétation'
      };
      expect(mediaCategoryService.create).toHaveBeenCalledWith(expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/media-categories']);
      expect(toastService.success).toHaveBeenCalledWith(`La catégorie de média a été créée`);
    });
  });

  describe('in update mode', () => {
    beforeEach(() => {
      const route = stubRoute({
        params: { mediaCategoryId: '41' }
      });
      prepare(route);

      mediaCategoryService.get.and.returnValue(
        of({
          id: 41,
          name: 'Espèces menacées'
        })
      );

      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Modifier une catégorie de média`);
    });

    it('should display a filled form', () => {
      expect(tester.name).toHaveValue('Espèces menacées');
    });

    it('should update the category', () => {
      tester.name.fillWith('Végétation');

      mediaCategoryService.update.and.returnValue(of(undefined));
      tester.saveButton.click();

      const expectedCommand: MediaCategoryCommand = {
        name: 'Végétation'
      };
      expect(mediaCategoryService.update).toHaveBeenCalledWith(41, expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/media-categories']);
      expect(toastService.success).toHaveBeenCalledWith(`La catégorie de média a été modifiée`);
    });
  });
});
