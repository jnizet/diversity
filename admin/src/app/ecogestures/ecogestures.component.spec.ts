import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ComponentTester, TestButton } from 'ngx-speculoos';
import { EMPTY, of } from 'rxjs';

import { EcogesturesComponent } from './ecogestures.component';
import { EcogestureService } from '../ecogesture.service';
import { ConfirmationService } from '../confirmation.service';
import { ToastService } from '../toast.service';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { Ecogesture } from '../ecogesture.model';
import { NgbTestingModule } from '../ngb-testing.module';

class EcogesturesComponentTester extends ComponentTester<EcogesturesComponent> {
  constructor() {
    super(EcogesturesComponent);
  }

  get ecogestures() {
    return this.elements('.ecogesture');
  }

  get createLink() {
    return this.element('#create-ecogesture');
  }

  get deleteButtons() {
    return this.elements('.delete-ecogesture-button') as Array<TestButton>;
  }
}

describe('EcogesturesComponent', () => {
  let tester: EcogesturesComponentTester;
  let ecogestureService: jasmine.SpyObj<EcogestureService>;
  let confirmationService: jasmine.SpyObj<ConfirmationService>;
  let toastService: jasmine.SpyObj<ToastService>;

  beforeEach(() => {
    ecogestureService = jasmine.createSpyObj<EcogestureService>('EcogestureService', ['list', 'delete']);
    confirmationService = jasmine.createSpyObj<ConfirmationService>('ConfirmationService', ['confirm']);
    toastService = jasmine.createSpyObj<ToastService>('ToastService', ['success']);

    TestBed.configureTestingModule({
      imports: [FontAwesomeModule, NgbModalModule, NgbTestingModule, RouterTestingModule],
      declarations: [EcogesturesComponent],
      providers: [
        { provide: EcogestureService, useValue: ecogestureService },
        { provide: ConfirmationService, useValue: confirmationService },
        { provide: ToastService, useValue: toastService }
      ]
    });

    tester = new EcogesturesComponentTester();
  });

  it('should not display anything until ecogestures are available', () => {
    ecogestureService.list.and.returnValue(EMPTY);
    tester.detectChanges();

    expect(tester.ecogestures.length).toBe(0);
    expect(tester.createLink).toBeNull();
  });

  it('should display ecogestures', () => {
    const ecogestures: Array<Ecogesture> = [
      {
        id: 1,
        slug: 'Ecogesture1'
      },
      {
        id: 2,
        slug: 'Ecogesture2'
      }
    ];

    ecogestureService.list.and.returnValue(of(ecogestures));
    tester.detectChanges();

    expect(tester.ecogestures.length).toBe(2);
    expect(tester.ecogestures[0]).toContainText('Ecogesture1');
    expect(tester.ecogestures[1]).toContainText('Ecogesture2');
    expect(tester.createLink).not.toBeNull();
  });

  it('should delete after confirmation and reload', () => {
    const ecogestures: Array<Ecogesture> = [
      {
        id: 1,
        slug: 'Ecogesture1'
      },
      {
        id: 2,
        slug: 'Ecogesture2'
      }
    ];

    ecogestureService.list.and.returnValues(of(ecogestures), of([ecogestures[1]]));
    tester.detectChanges();

    confirmationService.confirm.and.returnValue(of(undefined));
    ecogestureService.delete.and.returnValue(of(undefined));

    tester.deleteButtons[0].click();

    expect(tester.ecogestures.length).toBe(1);
    expect(ecogestureService.delete).toHaveBeenCalledWith(1);
    expect(toastService.success).toHaveBeenCalled();
  });
});
