import { TestBed } from '@angular/core/testing';

import { PageService } from './page.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Page, PageCommand } from './page.model';

describe('PageService', () => {
  let service: PageService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(PageService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should get a page with its values', () => {
    let actual: Page = null;

    service.getValues(42).subscribe(page => (actual = page));

    const expected = { id: 42 } as Page;
    http.expectOne({ method: 'GET', url: '/api/pages/42' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should get a page model (with empty values)', () => {
    let actual: Page = null;

    service.getModel('home').subscribe(page => (actual = page));

    const expected = { id: 42 } as Page;
    http.expectOne({ method: 'GET', url: '/api/pages/models/home' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should update', () => {
    let done = false;

    const command = { title: 'foo' } as PageCommand;
    service.update(42, command).subscribe(() => (done = true));

    const testRequest = http.expectOne({ method: 'PUT', url: '/api/pages/42' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(null);
    expect(done).toBe(true);
  });
});
