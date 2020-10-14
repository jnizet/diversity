import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Indicator, IndicatorCommand, ValuedIndicator } from './indicator.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class IndicatorService {
  constructor(private http: HttpClient) {}

  list(): Observable<Array<Indicator>> {
    return this.http.get<Array<Indicator>>('/api/indicators');
  }

  get(indicatorId: number): Observable<Indicator> {
    return this.http.get<Indicator>(`/api/indicators/${indicatorId}`);
  }

  getValues(biomId: string): Observable<ValuedIndicator> {
    return this.http.get<ValuedIndicator>(`/api/indicators/${biomId}/values`);
  }

  create(command: IndicatorCommand): Observable<Indicator> {
    return this.http.post<Indicator>('/api/indicators', command);
  }

  update(indicatorId: number, command: IndicatorCommand): Observable<void> {
    return this.http.put<void>(`/api/indicators/${indicatorId}`, command);
  }

  delete(indicatorId: number): Observable<void> {
    return this.http.delete<void>(`/api/indicators/${indicatorId}`);
  }
}
