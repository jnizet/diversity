import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MediaCommand } from './media.model';

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  constructor(private http: HttpClient) {}

  update(mediaId: number, command: MediaCommand): Observable<void> {
    return this.http.put<void>(`/api/media/${mediaId}`, command);
  }
}
