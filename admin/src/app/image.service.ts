import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Image } from './image.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  importedImages: {
    imageId: number;
    isLoading: boolean;
  }[] = [];
  constructor(private http: HttpClient) {}

  createImage(file: File, multiSize: boolean, document: boolean): Observable<Image> {
    const body = new FormData();
    body.set('file', file);
    const params = { multisize: `${multiSize}`, document: `${document}` };
    return this.http.post<Image>('/api/images', body, { params });
  }

  importImage(imageId: number, multiSize: boolean, document: boolean): Observable<Image> {
    const params = { multisize: `${multiSize}`, document: `${document}` };
    return this.http.get<Image>(`/api/images/import/${imageId}/image`, { params });
  }

  addImportedImage(imageId: number) {
    this.importedImages.push({ imageId, isLoading: true });
  }

  setImageLoadingToFalse(imageId: number) {
    const imageIndex = this.importedImages.findIndex(importedImage => importedImage.imageId === imageId);
    if (imageIndex !== -1) {
      this.importedImages[imageIndex].isLoading = false;
      if (!this.importedImages.find(importedImage => importedImage.isLoading)) {
        this.importedImages = [];
      }
    }
  }
}
