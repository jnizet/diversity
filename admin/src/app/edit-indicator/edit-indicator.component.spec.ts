import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of, Subject } from 'rxjs';
import { ValdemortModule } from 'ngx-valdemort';
import { ComponentTester, fakeRoute, fakeSnapshot } from 'ngx-speculoos';
import { EditIndicatorComponent } from './edit-indicator.component';
import { IndicatorService } from '../indicator.service';
import { ToastService } from '../toast.service';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { Indicator, IndicatorCommand, IndicatorValue } from '../indicator.model';
import { IndicatorCategoryService } from '../indicator-category.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { EcogestureService } from '../ecogesture.service';

class EditIndicatorComponentTester extends ComponentTester<EditIndicatorComponent> {
  constructor() {
    super(EditIndicatorComponent);
  }

  get title() {
    return this.element('h1');
  }

  get slug() {
    return this.input('#slug');
  }

  get biomId() {
    return this.input('#biom-id');
  }

  get firstCategory() {
    return this.select('#category-0');
  }

  get secondCategory() {
    return this.select('#category-1');
  }

  get addCategoryButton() {
    return this.button('.add-category-button');
  }

  get removeCategoryButton() {
    return this.button('.remove-category-button');
  }

  get firstEcogesture() {
    return this.select('#ecogesture-0');
  }

  get secondEcogesture() {
    return this.select('#ecogesture-1');
  }

  get addEcogestureButton() {
    return this.button('.add-ecogesture-button');
  }

  get removeEcogestureButton() {
    return this.button('.remove-ecogesture-button');
  }

  get fetchValuesButton() {
    return this.button('#fetch-values');
  }

  get spinner() {
    return this.element('#fetching-spinner');
  }

  get noData() {
    return this.element('#no-data');
  }

  get values() {
    return this.elements('.value');
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }

  get saveButton() {
    return this.button('#save-button');
  }
}

describe('EditIndicatorComponent', () => {
  let tester: EditIndicatorComponentTester;
  let indicatorService: jasmine.SpyObj<IndicatorService>;
  let indicatorCategoryService: jasmine.SpyObj<IndicatorCategoryService>;
  let ecogestureService: jasmine.SpyObj<EcogestureService>;
  let router: Router;
  let toastService: jasmine.SpyObj<ToastService>;
  const vegetation = { id: 101, name: 'Végétation' };
  const separateWaste = { id: 301, slug: 'trier-ses-dechets' };

  function prepare(route: ActivatedRoute) {
    indicatorService = jasmine.createSpyObj<IndicatorService>('IndicatorService', ['get', 'create', 'update', 'getValues']);
    indicatorCategoryService = jasmine.createSpyObj<IndicatorCategoryService>('IndicatorCategoryService', ['list']);
    ecogestureService = jasmine.createSpyObj<EcogestureService>('EcogestureService', ['list']);
    toastService = jasmine.createSpyObj<ToastService>('ToastService', ['success']);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule, RouterTestingModule, FontAwesomeModule],
      declarations: [EditIndicatorComponent, ValidationDefaultsComponent],
      providers: [
        { provide: IndicatorService, useValue: indicatorService },
        { provide: IndicatorCategoryService, useValue: indicatorCategoryService },
        { provide: EcogestureService, useValue: ecogestureService },
        { provide: ActivatedRoute, useValue: route },
        { provide: ToastService, useValue: toastService }
      ]
    });

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    indicatorCategoryService.list.and.returnValue(of([vegetation, { id: 102, name: 'Vie animale' }]));
    ecogestureService.list.and.returnValue(of([separateWaste, { id: 302, slug: 'proteger-les-coraux' }]));

    tester = new EditIndicatorComponentTester();
  }

  describe('in create mode', () => {
    beforeEach(() => {
      const route = fakeRoute({
        snapshot: fakeSnapshot({
          params: {}
        })
      });
      prepare(route);
      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Créer un indicateur`);
    });

    it('should display an empty form', () => {
      expect(tester.slug).toHaveValue('');
      expect(tester.biomId).toHaveValue('');
      expect(tester.firstCategory).toHaveSelectedLabel('');
      expect(tester.secondCategory).toBeNull();
      expect(tester.firstEcogesture).toHaveSelectedLabel('');
      expect(tester.secondEcogesture).toBeNull();
    });

    it('should not save if error', () => {
      expect(tester.errors.length).toBe(0);

      tester.saveButton.click();

      expect(tester.errors.length).toBe(3);
      expect(tester.errors[0]).toContainText('Le slug est obligatoire');
      expect(tester.errors[1]).toContainText(`L'identifiant BIOM est obligatoire`);
      expect(tester.errors[2]).toContainText(`Au moins une catégorie est nécessaire`);
      expect(indicatorService.create).not.toHaveBeenCalled();

      tester.slug.fillWith('aT-2');
      expect(tester.errors.length).toBe(3);
      expect(tester.errors[0]).toContainText('Le slug doit avoir la forme mot-autre-mot');

      tester.slug.fillWith('at-2');
      expect(tester.errors.length).toBe(2);
    });

    it('should create an indicator', () => {
      tester.slug.fillWith('especes-envahissantes');
      tester.biomId.fillWith('biom-41');
      tester.firstCategory.selectLabel('Vie animale');
      tester.firstEcogesture.selectLabel('proteger-les-coraux');

      indicatorService.create.and.returnValue(of({} as Indicator));
      tester.saveButton.click();

      const expectedCommand: IndicatorCommand = {
        slug: 'especes-envahissantes',
        biomId: 'biom-41',
        categoryIds: [102],
        ecogestureIds: [302]
      };
      expect(indicatorService.create).toHaveBeenCalledWith(expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/indicators']);
      expect(toastService.success).toHaveBeenCalledWith(`L'indicateur a été créé`);
    });
  });

  describe('in update mode', () => {
    beforeEach(() => {
      const route = fakeRoute({
        snapshot: fakeSnapshot({
          params: { indicatorId: '41' }
        })
      });
      prepare(route);

      indicatorService.get.and.returnValue(
        of({
          id: 41,
          slug: 'especes-envahissantes',
          biomId: 'biom-41',
          categories: [vegetation],
          ecogestures: [separateWaste]
        })
      );

      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Modifier un indicateur`);
    });

    it('should display a filled form', () => {
      expect(tester.slug).toHaveValue('especes-envahissantes');
    });

    it('should update the indicator', () => {
      tester.slug.fillWith('deforestation');

      // add a second category
      expect(tester.removeCategoryButton).toBeNull();
      tester.addCategoryButton.click();
      // the available categories do not contain the already selected one
      expect(tester.secondCategory.optionLabels).toEqual(['', 'Vie animale']);
      tester.secondCategory.selectLabel('Vie animale');
      // remove the first category
      expect(tester.addCategoryButton).toBeNull();
      tester.removeCategoryButton.click();
      expect(tester.secondCategory).toBeNull();
      expect(tester.firstCategory).toHaveSelectedLabel('Vie animale');
      expect(tester.removeCategoryButton).toBeNull();
      // add a second category again
      tester.addCategoryButton.click();
      expect(tester.secondCategory.optionLabels).toEqual(['', 'Végétation']);
      tester.secondCategory.selectLabel('Végétation');

      // add a second ecogesture
      expect(tester.removeEcogestureButton).not.toBeNull();
      tester.addEcogestureButton.click();
      // the available ecogestures do not contain the already selected one
      expect(tester.secondEcogesture.optionLabels).toEqual(['', 'proteger-les-coraux']);
      tester.secondEcogesture.selectLabel('proteger-les-coraux');
      // remove the first ecogesture
      expect(tester.addEcogestureButton).not.toBeNull();
      tester.removeEcogestureButton.click();
      expect(tester.secondEcogesture).toBeNull();
      expect(tester.firstEcogesture).toHaveSelectedLabel('proteger-les-coraux');
      expect(tester.removeEcogestureButton).not.toBeNull();
      // add a second ecogesture again
      tester.addEcogestureButton.click();
      expect(tester.secondEcogesture.optionLabels).toEqual(['', 'trier-ses-dechets']);
      tester.secondEcogesture.selectLabel('trier-ses-dechets');

      indicatorService.update.and.returnValue(of(undefined));
      tester.saveButton.click();

      const expectedCommand: IndicatorCommand = {
        slug: 'deforestation',
        biomId: 'biom-41',
        categoryIds: [102, 101],
        ecogestureIds: [302, 301]
      };
      expect(indicatorService.update).toHaveBeenCalledWith(41, expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/indicators']);
      expect(toastService.success).toHaveBeenCalledWith(`L'indicateur a été modifié`);
    });

    describe('should fetch values', () => {
      let valuesSubject: Subject<Array<IndicatorValue>>;

      beforeEach(() => {
        valuesSubject = new Subject<Array<IndicatorValue>>();
        indicatorService.getValues.and.returnValue(valuesSubject);

        tester.componentInstance.indicatorValues = [
          {
            territory: 'Outre-mer',
            value: 10,
            unit: '%'
          }
        ];
        tester.detectChanges();

        // nothing at first
        expect(tester.noData).toBeNull();
        expect(tester.spinner).toBeNull();
        expect(tester.values.length).toBe(1);

        // fetch the values
        tester.fetchValuesButton.click();
        expect(tester.spinner).not.toBeNull();
      });

      it('and display them', () => {
        // return values
        valuesSubject.next([
          { territory: 'OUTRE_MER', value: 28.7, unit: '%' },
          { territory: 'REUNION', value: 13.2, unit: '%' }
        ]);
        valuesSubject.complete();
        tester.detectChanges();
        expect(tester.noData).toBeNull();
        expect(tester.spinner).toBeNull();
        expect(tester.values.length).toEqual(2);
        expect(tester.values[0]).toContainText('OUTRE_MER');
        expect(tester.values[0]).toContainText('28.7\u00a0%');
        expect(tester.values[1]).toContainText('REUNION');
        expect(tester.values[1]).toContainText('13.2\u00a0%');
      });

      it('and display a message on error', () => {
        // error on fetch
        valuesSubject.error('oops');
        tester.detectChanges();

        expect(tester.noData).toContainText(`Les valeurs n'ont pas pu être récupérées pour cet indicateur`);
        expect(tester.spinner).toBeNull();
        expect(tester.values.length).toBe(0);
      });
    });
  });
});
