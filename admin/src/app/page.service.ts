import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, PageCommand } from './page.model';

@Injectable({
  providedIn: 'root'
})
export class PageService {
  constructor(private http: HttpClient) {}

  get(pageId: number): Observable<Page> {
    return this.http.get<Page>(`/api/pages/${pageId}`);
  }

  update(pageId: number, command: PageCommand): Observable<void> {
    return this.http.put<void>(`/api/pages/${pageId}`, command);
  }
}
