import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IndicatorCategory, IndicatorCategoryCommand } from './indicator-category.model';

@Injectable({
  providedIn: 'root'
})
export class IndicatorCategoryService {
  constructor(private http: HttpClient) {}

  /**
   * Lists the categories.
   * Note that this method does not return a page as there are a limited number of categories.
   */
  list(): Observable<Array<IndicatorCategory>> {
    return this.http.get<Array<IndicatorCategory>>('api/indicator-categories');
  }

  get(categoryId: number): Observable<IndicatorCategory> {
    return this.http.get<IndicatorCategory>(`api/indicator-categories/${categoryId}`);
  }

  create(command: IndicatorCategoryCommand): Observable<IndicatorCategory> {
    return this.http.post<IndicatorCategory>('api/indicator-categories', command);
  }

  update(categoryId: number, command: IndicatorCategoryCommand): Observable<void> {
    return this.http.put<void>(`api/indicator-categories/${categoryId}`, command);
  }

  delete(categoryId: number): Observable<void> {
    return this.http.delete<void>(`api/indicator-categories/${categoryId}`);
  }
}
