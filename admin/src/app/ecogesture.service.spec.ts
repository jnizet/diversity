import { TestBed } from '@angular/core/testing';

import { EcogestureService } from './ecogesture.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Ecogesture, EcogestureCommand } from './ecogesture.model';

describe('EcogestureService', () => {
  let service: EcogestureService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(EcogestureService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should list ecogestures', () => {
    let actual: Array<Ecogesture> = null;

    service.list().subscribe(ecogestures => (actual = ecogestures));

    const expected = [] as Array<Ecogesture>;
    http.expectOne({ method: 'GET', url: '/api/ecogestures' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should get', () => {
    let actual: Ecogesture = null;

    service.get(42).subscribe(category => (actual = category));

    const expected = { id: 42 } as Ecogesture;
    http.expectOne({ method: 'GET', url: '/api/ecogestures/42' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should create', () => {
    let actual: Ecogesture = null;

    const command = { slug: 'foo' } as EcogestureCommand;
    service.create(command).subscribe(category => (actual = category));

    const expected = { id: 42 } as Ecogesture;
    const testRequest = http.expectOne({ method: 'POST', url: '/api/ecogestures' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(expected);
    expect(actual).toBe(expected);
  });

  it('should update', () => {
    let done = false;

    const command = { slug: 'foo' } as EcogestureCommand;
    service.update(42, command).subscribe(() => (done = true));

    const testRequest = http.expectOne({ method: 'PUT', url: '/api/ecogestures/42' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(null);
    expect(done).toBe(true);
  });

  it('should delete', () => {
    let done = false;

    service.delete(42).subscribe(() => (done = true));

    http.expectOne({ method: 'DELETE', url: '/api/ecogestures/42' }).flush(null);
    expect(done).toBe(true);
  });
});
