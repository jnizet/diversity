import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IndicatorService } from './indicator.service';
import { Indicator, IndicatorCommand } from './indicator.model';

describe('IndicatorService', () => {
  let service: IndicatorService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(IndicatorService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should list indicators', () => {
    let actual: Array<Indicator> = null;

    service.list().subscribe(indicators => (actual = indicators));

    const expected = [] as Array<Indicator>;
    http.expectOne({ method: 'GET', url: 'api/indicators' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should get', () => {
    let actual: Indicator = null;

    service.get(42).subscribe(indicator => (actual = indicator));

    const expected = { id: 42 } as Indicator;
    http.expectOne({ method: 'GET', url: 'api/indicators/42' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should create', () => {
    let actual: Indicator = null;

    const command = { slug: 'foo' } as IndicatorCommand;
    service.create(command).subscribe(indicator => (actual = indicator));

    const expected = { id: 42 } as Indicator;
    const testRequest = http.expectOne({ method: 'POST', url: 'api/indicators' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(expected);
    expect(actual).toBe(expected);
  });

  it('should update', () => {
    let done = false;

    const command = { slug: 'foo' } as IndicatorCommand;
    service.update(42, command).subscribe(() => (done = true));

    const testRequest = http.expectOne({ method: 'PUT', url: 'api/indicators/42' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(null);
    expect(done).toBe(true);
  });

  it('should delete', () => {
    let done = false;

    service.delete(42).subscribe(() => (done = true));

    http.expectOne({ method: 'DELETE', url: 'api/indicators/42' }).flush(null);
    expect(done).toBe(true);
  });
});
