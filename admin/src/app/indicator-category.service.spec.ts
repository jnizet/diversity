import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IndicatorCategoryService } from './indicator-category.service';
import { IndicatorCategory, IndicatorCategoryCommand } from './indicator-category.model';

describe('IndicatorCategoryService', () => {
  let service: IndicatorCategoryService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(IndicatorCategoryService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should list categories', () => {
    let actual: Array<IndicatorCategory> = null;

    service.list().subscribe(categories => (actual = categories));

    const expected = [] as Array<IndicatorCategory>;
    http.expectOne({ method: 'GET', url: '/api/indicator-categories' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should get', () => {
    let actual: IndicatorCategory = null;

    service.get(42).subscribe(category => (actual = category));

    const expected = { id: 42 } as IndicatorCategory;
    http.expectOne({ method: 'GET', url: '/api/indicator-categories/42' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should create', () => {
    let actual: IndicatorCategory = null;

    const command = { name: 'foo' } as IndicatorCategoryCommand;
    service.create(command).subscribe(categorie => (actual = categorie));

    const expected = { id: 42 } as IndicatorCategory;
    const testRequest = http.expectOne({ method: 'POST', url: '/api/indicator-categories' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(expected);
    expect(actual).toBe(expected);
  });

  it('should update', () => {
    let done = false;

    const command = { name: 'foo' } as IndicatorCategoryCommand;
    service.update(42, command).subscribe(() => (done = true));

    const testRequest = http.expectOne({ method: 'PUT', url: '/api/indicator-categories/42' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(null);
    expect(done).toBe(true);
  });

  it('should delete', () => {
    let done = false;

    service.delete(42).subscribe(() => (done = true));

    http.expectOne({ method: 'DELETE', url: '/api/indicator-categories/42' }).flush(null);
    expect(done).toBe(true);
  });
});
