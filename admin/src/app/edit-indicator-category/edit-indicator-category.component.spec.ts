import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { ValdemortModule } from 'ngx-valdemort';
import { ComponentTester, createMock, stubRoute } from 'ngx-speculoos';
import { EditIndicatorCategoryComponent } from './edit-indicator-category.component';
import { IndicatorCategoryService } from '../indicator-category.service';
import { ToastService } from '../toast.service';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { IndicatorCategory, IndicatorCategoryCommand } from '../indicator-category.model';

class EditIndicatorCategoryComponentTester extends ComponentTester<EditIndicatorCategoryComponent> {
  constructor() {
    super(EditIndicatorCategoryComponent);
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

describe('EditIndicatorCategoryComponent', () => {
  let tester: EditIndicatorCategoryComponentTester;
  let indicatorCategoryService: jasmine.SpyObj<IndicatorCategoryService>;
  let router: Router;
  let toastService: jasmine.SpyObj<ToastService>;

  function prepare(route: ActivatedRoute) {
    indicatorCategoryService = createMock(IndicatorCategoryService);
    toastService = createMock(ToastService);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule, RouterTestingModule],
      declarations: [EditIndicatorCategoryComponent, ValidationDefaultsComponent],
      providers: [
        { provide: IndicatorCategoryService, useValue: indicatorCategoryService },
        { provide: ActivatedRoute, useValue: route },
        { provide: ToastService, useValue: toastService }
      ]
    });

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new EditIndicatorCategoryComponentTester();
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
      expect(tester.title).toContainText(`Créer une catégorie d'indicateur`);
    });

    it('should display an empty form', () => {
      expect(tester.name).toHaveValue('');
    });

    it('should not save if error', () => {
      expect(tester.errors.length).toBe(0);

      tester.saveButton.click();

      expect(tester.errors.length).toBe(1);
      expect(tester.errors[0]).toContainText('Le nom est obligatoire');
      expect(indicatorCategoryService.create).not.toHaveBeenCalled();
    });

    it('should create a category', () => {
      tester.name.fillWith('Végétation');

      indicatorCategoryService.create.and.returnValue(of({} as IndicatorCategory));
      tester.saveButton.click();

      const expectedCommand: IndicatorCategoryCommand = {
        name: 'Végétation'
      };
      expect(indicatorCategoryService.create).toHaveBeenCalledWith(expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/indicator-categories']);
      expect(toastService.success).toHaveBeenCalledWith(`La catégorie d'indicateur a été créée`);
    });
  });

  describe('in update mode', () => {
    beforeEach(() => {
      const route = stubRoute({
        params: { indicatorCategoryId: '41' }
      });
      prepare(route);

      indicatorCategoryService.get.and.returnValue(
        of({
          id: 41,
          name: 'Espèces menacées'
        })
      );

      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Modifier une catégorie d'indicateur`);
    });

    it('should display a filled form', () => {
      expect(tester.name).toHaveValue('Espèces menacées');
    });

    it('should update the category', () => {
      tester.name.fillWith('Végétation');

      indicatorCategoryService.update.and.returnValue(of(undefined));
      tester.saveButton.click();

      const expectedCommand: IndicatorCategoryCommand = {
        name: 'Végétation'
      };
      expect(indicatorCategoryService.update).toHaveBeenCalledWith(41, expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/indicator-categories']);
      expect(toastService.success).toHaveBeenCalledWith(`La catégorie d'indicateur a été modifiée`);
    });
  });
});
