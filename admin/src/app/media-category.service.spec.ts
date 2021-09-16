import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { MediaCategoryService } from './media-category.service';
import { MediaCategory, MediaCategoryCommand } from './media-category.model';

describe('MediaCategoryService', () => {
  let service: MediaCategoryService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(MediaCategoryService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should list categories', () => {
    let actual: Array<MediaCategory> = null;

    service.list().subscribe(categories => (actual = categories));

    const expected = [] as Array<MediaCategory>;
    http.expectOne({ method: 'GET', url: '/api/media-categories' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should get', () => {
    let actual: MediaCategory = null;

    service.get(42).subscribe(category => (actual = category));

    const expected = { id: 42 } as MediaCategory;
    http.expectOne({ method: 'GET', url: '/api/media-categories/42' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should create', () => {
    let actual: MediaCategory = null;

    const command = { name: 'foo' } as MediaCategoryCommand;
    service.create(command).subscribe(category => (actual = category));

    const expected = { id: 42 } as MediaCategory;
    const testRequest = http.expectOne({ method: 'POST', url: '/api/media-categories' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(expected);
    expect(actual).toBe(expected);
  });

  it('should update', () => {
    let done = false;

    const command = { name: 'foo' } as MediaCategoryCommand;
    service.update(42, command).subscribe(() => (done = true));

    const testRequest = http.expectOne({ method: 'PUT', url: '/api/media-categories/42' });
    expect(testRequest.request.body).toBe(command);
    testRequest.flush(null);
    expect(done).toBe(true);
  });

  it('should delete', () => {
    let done = false;

    service.delete(42).subscribe(() => (done = true));

    http.expectOne({ method: 'DELETE', url: '/api/media-categories/42' }).flush(null);
    expect(done).toBe(true);
  });
});
