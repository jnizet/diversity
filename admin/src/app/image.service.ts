import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Image } from './image.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  constructor(private http: HttpClient) {}

  createImage(file: File, multiSize: boolean): Observable<Image> {
    const body = new FormData();
    body.set('file', file);
    const params = multiSize ? { multisize: 'true' } : {};
    return this.http.post<Image>('/api/images', body, { params });
  }
}
