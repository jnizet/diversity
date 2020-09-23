import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

export interface Toast {
  message: string;
  type: 'success' | 'error';
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toastSubject = new Subject<Toast>();

  constructor() {}

  error(message: string) {
    this.signal(message, 'error');
  }

  success(message: string) {
    this.signal(message, 'success');
  }

  toasts(): Observable<Toast> {
    return this.toastSubject.asObservable();
  }

  private signal(message: string, type: 'success' | 'error') {
    this.toastSubject.next({ message, type });
  }
}
