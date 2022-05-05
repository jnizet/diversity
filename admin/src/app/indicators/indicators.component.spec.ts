import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ComponentTester, createMock, TestButton } from 'ngx-speculoos';
import { EMPTY, of } from 'rxjs';

import { IndicatorsComponent } from './indicators.component';
import { IndicatorService } from '../indicator.service';
import { ConfirmationService } from '../confirmation.service';
import { ToastService } from '../toast.service';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { Indicator } from '../indicator.model';
import { IndicatorCategory } from '../indicator-category.model';
import { NgbTestingModule } from '../ngb-testing.module';

class IndicatorsComponentTester extends ComponentTester<IndicatorsComponent> {
  constructor() {
    super(IndicatorsComponent);
  }

  get indicators() {
    return this.elements('.indicator');
  }

  get createLink() {
    return this.element('#create-indicator');
  }

  get deleteButtons() {
    return this.elements('.delete-indicator-button') as Array<TestButton>;
  }
}

describe('IndicatorsComponent', () => {
  let tester: IndicatorsComponentTester;
  let indicatorService: jasmine.SpyObj<IndicatorService>;
  let confirmationService: jasmine.SpyObj<ConfirmationService>;
  let toastService: jasmine.SpyObj<ToastService>;
  const vegetation: IndicatorCategory = {
    id: 1,
    name: 'Végétation'
  };

  beforeEach(() => {
    indicatorService = createMock(IndicatorService);
    confirmationService = createMock(ConfirmationService);
    toastService = createMock(ToastService);

    TestBed.configureTestingModule({
      imports: [FontAwesomeModule, NgbModalModule, NgbTestingModule, RouterTestingModule],
      declarations: [IndicatorsComponent],
      providers: [
        { provide: IndicatorService, useValue: indicatorService },
        { provide: ConfirmationService, useValue: confirmationService },
        { provide: ToastService, useValue: toastService }
      ]
    });

    tester = new IndicatorsComponentTester();
  });

  it('should not display anything until indicators are available', () => {
    indicatorService.list.and.returnValue(EMPTY);
    tester.detectChanges();

    expect(tester.indicators.length).toBe(0);
    expect(tester.createLink).toBeNull();
  });

  it('should display indicators', () => {
    const indicators: Array<Indicator> = [
      {
        id: 1,
        biomId: 'biom_1',
        slug: 'deforestation',
        categories: [vegetation],
        ecogestures: [],
        isRounded: false,
        rank: 1
      },
      {
        id: 2,
        biomId: 'biom_2',
        slug: 'especes-menacees',
        categories: [],
        ecogestures: [],
        isRounded: true,
        rank: 2
      }
    ];

    indicatorService.list.and.returnValue(of(indicators));
    tester.detectChanges();

    expect(tester.indicators.length).toBe(2);
    expect(tester.indicators[0]).toContainText('deforestation');
    expect(tester.indicators[0]).toContainText('biom_1');
    expect(tester.indicators[0]).toContainText('Végétation');
    expect(tester.indicators[0]).not.toContainText('arrondi');
    expect(tester.indicators[1]).toContainText('especes-menacees');
    expect(tester.indicators[1]).toContainText('biom_2');
    expect(tester.indicators[1]).not.toContainText('Végétation');
    expect(tester.indicators[1]).toContainText('arrondi');
    expect(tester.createLink).not.toBeNull();
  });

  it('should delete after confirmation and reload', () => {
    const indicators: Array<Indicator> = [
      {
        id: 1,
        biomId: 'biom_1',
        slug: 'deforestation',
        categories: [vegetation],
        ecogestures: [],
        isRounded: false,
        rank: 1
      },
      {
        id: 2,
        biomId: 'biom_2',
        slug: 'especes-menacees',
        categories: [],
        ecogestures: [],
        isRounded: false,
        rank: 2
      }
    ];

    indicatorService.list.and.returnValues(of(indicators), of([indicators[1]]));
    tester.detectChanges();

    confirmationService.confirm.and.returnValue(of(undefined));
    indicatorService.delete.and.returnValue(of(undefined));

    tester.deleteButtons[0].click();

    expect(tester.indicators.length).toBe(1);
    expect(indicatorService.delete).toHaveBeenCalledWith(1);
    expect(toastService.success).toHaveBeenCalled();
  });
});
