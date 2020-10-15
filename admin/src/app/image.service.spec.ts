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

    service.createImage(file, false).subscribe(image => (actual = image));

    const expected = {} as Image;
    const testRequest = http.expectOne({ method: 'POST', url: '/api/images' });
    expect(testRequest.request.body instanceof FormData).toBeTrue();
    expect((testRequest.request.body as FormData).has('file')).toBeTrue();
    testRequest.flush(expected);
    expect(actual).toBe(expected);
  });

  it('should create a multiSize image', () => {
    let actual: Image = null;

    const file = {} as File;

    service.createImage(file, true).subscribe(image => (actual = image));

    const expected = {} as Image;
    http.expectOne({ method: 'POST', url: '/api/images?multisize=true' }).flush(expected);
    expect(actual).toBe(expected);
  });
});
