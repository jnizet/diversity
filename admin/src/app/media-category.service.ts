import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MediaCategory, MediaCategoryCommand } from './media-category.model';

@Injectable({
  providedIn: 'root'
})
export class MediaCategoryService {
  constructor(private http: HttpClient) {}

  /**
   * Lists the categories.
   * Note that this method does not return a page as there are a limited number of categories.
   */
  list(): Observable<Array<MediaCategory>> {
    return this.http.get<Array<MediaCategory>>('/api/media-categories');
  }

  get(categoryId: number): Observable<MediaCategory> {
    return this.http.get<MediaCategory>(`/api/media-categories/${categoryId}`);
  }

  create(command: MediaCategoryCommand): Observable<MediaCategory> {
    return this.http.post<MediaCategory>('/api/media-categories', command);
  }

  update(categoryId: number, command: MediaCategoryCommand): Observable<void> {
    return this.http.put<void>(`/api/media-categories/${categoryId}`, command);
  }

  delete(categoryId: number): Observable<void> {
    return this.http.delete<void>(`/api/media-categories/${categoryId}`);
  }
}
