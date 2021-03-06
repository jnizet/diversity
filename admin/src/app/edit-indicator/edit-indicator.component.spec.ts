import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of, Subject } from 'rxjs';
import { ValdemortModule } from 'ngx-valdemort';
import { ComponentTester, createMock, stubRoute } from 'ngx-speculoos';
import { EditIndicatorComponent } from './edit-indicator.component';
import { IndicatorService } from '../indicator.service';
import { ToastService } from '../toast.service';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { Indicator, IndicatorCommand, ValuedIndicator } from '../indicator.model';
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

  get isRounded() {
    return this.input('#is-rounded');
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

  get indicatorSection() {
    return this.element('#indicator-section');
  }

  get closeIndicatorSection() {
    return this.indicatorSection.button('.btn-close');
  }

  get spinner() {
    return this.indicatorSection.element('#fetching-spinner');
  }

  get noData() {
    return this.indicatorSection.element('#no-data');
  }

  get valuedIndicator() {
    return this.indicatorSection.element('#valued-indicator');
  }

  get values() {
    return this.valuedIndicator.elements('.value');
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
  const vegetation = { id: 101, name: 'V??g??tation' };
  const separateWaste = { id: 301, slug: 'trier-ses-dechets' };

  function prepare(route: ActivatedRoute) {
    indicatorService = createMock(IndicatorService);
    indicatorCategoryService = createMock(IndicatorCategoryService);
    ecogestureService = createMock(EcogestureService);
    toastService = createMock(ToastService);

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
      const route = stubRoute({
        params: {}
      });
      prepare(route);
      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Cr??er un indicateur`);
    });

    it('should display an empty form', () => {
      expect(tester.slug).toHaveValue('');
      expect(tester.biomId).toHaveValue('');
      expect(tester.firstCategory).toHaveSelectedLabel('');
      expect(tester.secondCategory).toBeNull();
      expect(tester.firstEcogesture).toHaveSelectedLabel('');
      expect(tester.secondEcogesture).toBeNull();
      expect(tester.indicatorSection).toBeNull();
    });

    it('should not save if error', () => {
      expect(tester.errors.length).toBe(0);

      tester.saveButton.click();

      expect(tester.errors.length).toBe(3);
      expect(tester.errors[0]).toContainText('Le slug est obligatoire');
      expect(tester.errors[1]).toContainText(`L'identifiant BIOM est obligatoire`);
      expect(tester.errors[2]).toContainText(`Au moins une cat??gorie est n??cessaire`);
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
        rank: null,
        slug: 'especes-envahissantes',
        biomId: 'biom-41',
        isRounded: false,
        categoryIds: [102],
        ecogestureIds: [302]
      };
      expect(indicatorService.create).toHaveBeenCalledWith(expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/indicators']);
      expect(toastService.success).toHaveBeenCalledWith(`L'indicateur a ??t?? cr????`);
    });
  });

  describe('in update mode', () => {
    beforeEach(() => {
      const route = stubRoute({
        params: { indicatorId: '41' }
      });
      prepare(route);

      indicatorService.get.and.returnValue(
        of({
          id: 41,
          slug: 'especes-envahissantes',
          biomId: 'biom-41',
          isRounded: false,
          rank: 1,
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

      // should select rounded option
      expect(tester.isRounded.checked).toBe(false);
      tester.isRounded.click();
      expect(tester.isRounded.checked).toBe(true);

      // remove the first category
      expect(tester.addCategoryButton).toBeNull();
      tester.removeCategoryButton.click();
      expect(tester.secondCategory).toBeNull();
      expect(tester.firstCategory).toHaveSelectedLabel('Vie animale');
      expect(tester.removeCategoryButton).toBeNull();
      // add a second category again
      tester.addCategoryButton.click();
      expect(tester.secondCategory.optionLabels).toEqual(['', 'V??g??tation']);
      tester.secondCategory.selectLabel('V??g??tation');

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
        rank: 1,
        slug: 'deforestation',
        biomId: 'biom-41',
        categoryIds: [102, 101],
        ecogestureIds: [302, 301],
        isRounded: true
      };
      expect(indicatorService.update).toHaveBeenCalledWith(41, expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/indicators']);
      expect(toastService.success).toHaveBeenCalledWith(`L'indicateur a ??t?? modifi??`);
    });

    describe('should fetch values', () => {
      let valuedIndicatorSubject: Subject<ValuedIndicator>;

      beforeEach(() => {
        valuedIndicatorSubject = new Subject<ValuedIndicator>();
        indicatorService.getValues.and.returnValue(valuedIndicatorSubject);

        tester.componentInstance.valuedIndicator = {
          shortLabel: 'Something',
          values: [
            {
              territory: 'Outre-mer',
              value: 10,
              unit: '%'
            }
          ]
        };
        tester.detectChanges();

        // nothing at first except the old valued indicator
        expect(tester.noData).toBeNull();
        expect(tester.spinner).toBeNull();
        expect(tester.valuedIndicator).not.toBeNull();

        // fetch the values
        tester.fetchValuesButton.click();
        expect(tester.spinner).not.toBeNull();
      });

      it('and display them', () => {
        // return values
        valuedIndicatorSubject.next({
          shortLabel: 'Deforestation',
          values: [
            { territory: 'OUTRE_MER', value: 28.7, unit: '%' },
            { territory: 'REUNION', value: 13.2, unit: '%' }
          ]
        });
        valuedIndicatorSubject.complete();
        tester.detectChanges();
        expect(tester.noData).toBeNull();
        expect(tester.spinner).toBeNull();
        expect(tester.valuedIndicator).toContainText('Deforestation');
        expect(tester.values.length).toEqual(2);
        expect(tester.values[0]).toContainText('OUTRE_MER');
        expect(tester.values[0]).toContainText('28.7\u00a0%');
        expect(tester.values[1]).toContainText('REUNION');
        expect(tester.values[1]).toContainText('13.2\u00a0%');

        tester.closeIndicatorSection.click();
        expect(tester.indicatorSection).toBeNull();
      });

      it('and display a message on error', () => {
        // error on fetch
        valuedIndicatorSubject.error('oops');
        tester.detectChanges();

        expect(tester.noData).toContainText(`Les valeurs n'ont pas pu ??tre r??cup??r??es pour cet indicateur`);
        expect(tester.spinner).toBeNull();
        expect(tester.valuedIndicator).toBeNull();

        tester.closeIndicatorSection.click();
        expect(tester.indicatorSection).toBeNull();
      });
    });
  });
});
