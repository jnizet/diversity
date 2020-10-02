import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ecogesture, EcogestureCommand } from './ecogesture.model';

@Injectable({
  providedIn: 'root'
})
export class EcogestureService {
  constructor(private http: HttpClient) {}

  /**
   * Lists the ecogestures.
   * Note that this method does not return a page as there are a limited number of ecogestures.
   */
  list(): Observable<Array<Ecogesture>> {
    return this.http.get<Array<Ecogesture>>('/api/ecogestures');
  }

  get(ecogestureId: number): Observable<Ecogesture> {
    return this.http.get<Ecogesture>(`/api/ecogestures/${ecogestureId}`);
  }

  create(command: EcogestureCommand): Observable<Ecogesture> {
    return this.http.post<Ecogesture>('/api/ecogestures', command);
  }

  update(ecogestureId: number, command: EcogestureCommand): Observable<void> {
    return this.http.put<void>(`/api/ecogestures/${ecogestureId}`, command);
  }

  delete(ecogestureId: number): Observable<void> {
    return this.http.delete<void>(`/api/ecogestures/${ecogestureId}`);
  }
}
