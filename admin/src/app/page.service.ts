import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, PageCommand, PageLinks } from './page.model';

@Injectable({
  providedIn: 'root'
})
export class PageService {
  constructor(private http: HttpClient) {}

  /**
   * Gets a page with its metadata and element values.
   */
  getValues(pageId: number): Observable<Page> {
    return this.http.get<Page>(`/api/pages/${pageId}`);
  }

  /**
   * Gets a page model, with empty values for each of its elements.
   */
  getModel(pageModelName: string): Observable<Page> {
    return this.http.get<Page>(`/api/pages/models/${pageModelName}`);
  }

  update(pageId: number, command: PageCommand): Observable<void> {
    return this.http.put<void>(`/api/pages/${pageId}`, command);
  }

  create(pageModelName: string, command: PageCommand): Observable<Page> {
    return this.http.post<Page>(`/api/pages/models/${pageModelName}`, command);
  }

  getPageLinks(): Observable<PageLinks> {
    return this.http.get<PageLinks>('/api/pages/links');
  }
}
