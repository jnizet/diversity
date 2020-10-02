import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { ValdemortModule } from 'ngx-valdemort';
import { ComponentTester, fakeRoute, fakeSnapshot } from 'ngx-speculoos';
import { EditEcogestureComponent } from './edit-ecogesture.component';
import { EcogestureService } from '../ecogesture.service';
import { ToastService } from '../toast.service';
import { ValidationDefaultsComponent } from '../validation-defaults/validation-defaults.component';
import { Ecogesture, EcogestureCommand } from '../ecogesture.model';

class EditEcogestureComponentTester extends ComponentTester<EditEcogestureComponent> {
  constructor() {
    super(EditEcogestureComponent);
  }

  get title() {
    return this.element('h1');
  }

  get slug() {
    return this.input('#slug');
  }

  get errors() {
    return this.elements('.invalid-feedback div');
  }

  get saveButton() {
    return this.button('#save-button');
  }
}

describe('EditEcogestureComponent', () => {
  let tester: EditEcogestureComponentTester;
  let ecogestureService: jasmine.SpyObj<EcogestureService>;
  let router: Router;
  let toastService: jasmine.SpyObj<ToastService>;

  function prepare(route: ActivatedRoute) {
    ecogestureService = jasmine.createSpyObj<EcogestureService>('EcogestureService', ['get', 'create', 'update']);
    toastService = jasmine.createSpyObj<ToastService>('ToastService', ['success']);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ValdemortModule, RouterTestingModule],
      declarations: [EditEcogestureComponent, ValidationDefaultsComponent],
      providers: [
        { provide: EcogestureService, useValue: ecogestureService },
        { provide: ActivatedRoute, useValue: route },
        { provide: ToastService, useValue: toastService }
      ]
    });

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');

    TestBed.createComponent(ValidationDefaultsComponent).detectChanges();

    tester = new EditEcogestureComponentTester();
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
      expect(tester.title).toContainText(`Créer un éco-geste`);
    });

    it('should display an empty form', () => {
      expect(tester.slug).toHaveValue('');
    });

    it('should not save if error', () => {
      expect(tester.errors.length).toBe(0);

      tester.saveButton.click();

      expect(tester.errors.length).toBe(1);
      expect(tester.errors[0]).toContainText('Le slug est obligatoire');
      expect(ecogestureService.create).not.toHaveBeenCalled();
    });

    it('should create an ecogesture', () => {
      tester.slug.fillWith('trier-ses-dechets');

      ecogestureService.create.and.returnValue(of({} as Ecogesture));
      tester.saveButton.click();

      const expectedCommand: EcogestureCommand = {
        slug: 'trier-ses-dechets'
      };
      expect(ecogestureService.create).toHaveBeenCalledWith(expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/ecogestures']);
      expect(toastService.success).toHaveBeenCalledWith(`L'éco-geste a été créé`);
    });
  });

  describe('in update mode', () => {
    beforeEach(() => {
      const route = fakeRoute({
        snapshot: fakeSnapshot({
          params: { ecogestureId: '41' }
        })
      });
      prepare(route);

      ecogestureService.get.and.returnValue(
        of({
          id: 41,
          slug: 'proteger-les-coraux'
        })
      );

      tester.detectChanges();
    });

    it('should have a title', () => {
      expect(tester.title).toContainText(`Modifier un éco-geste`);
    });

    it('should display a filled form', () => {
      expect(tester.slug).toHaveValue('proteger-les-coraux');
    });

    it('should update the ecogesture', () => {
      tester.slug.fillWith('trier-ses-dechets');

      ecogestureService.update.and.returnValue(of(undefined));
      tester.saveButton.click();

      const expectedCommand: EcogestureCommand = {
        slug: 'trier-ses-dechets'
      };
      expect(ecogestureService.update).toHaveBeenCalledWith(41, expectedCommand);
      expect(router.navigate).toHaveBeenCalledWith(['/ecogestures']);
      expect(toastService.success).toHaveBeenCalledWith(`L'éco-geste a été modifié`);
    });
  });
});
