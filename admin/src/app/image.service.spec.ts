import { TestBed } from '@angular/core/testing';

import { ImageService } from './image.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Image } from './image.model';

describe('ImageService', () => {
  let service: ImageService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(ImageService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should create an image', () => {
    let actual: Image = null;

    const file = {} as File;

    service.createImage(file, false, false).subscribe(image => (actual = image));

    const expected = {} as Image;
    const testRequest = http.expectOne({ method: 'POST', url: '/api/images?multisize=false&document=false' });
    expect(testRequest.request.body instanceof FormData).toBeTrue();
    expect((testRequest.request.body as FormData).has('file')).toBeTrue();
    testRequest.flush(expected);
    expect(actual).toBe(expected);
  });

  it('should create a multiSize image', () => {
    let actual: Image = null;

    const file = {} as File;

    service.createImage(file, true, false).subscribe(image => (actual = image));

    const expected = {} as Image;
    http.expectOne({ method: 'POST', url: '/api/images?multisize=true&document=false' }).flush(expected);
    expect(actual).toBe(expected);
  });

  it('should create a document', () => {
    let actual: Image = null;

    const file = {} as File;

    service.createImage(file, false, true).subscribe(image => (actual = image));

    const expected = {} as Image;
    const testRequest = http.expectOne({ method: 'POST', url: '/api/images?multisize=false&document=true' });
    expect(testRequest.request.body instanceof FormData).toBeTrue();
    expect((testRequest.request.body as FormData).has('file')).toBeTrue();
    testRequest.flush(expected);
    expect(actual).toBe(expected);
  });
});
