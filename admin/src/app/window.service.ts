import { Injectable } from '@angular/core';

/**
 * Service used to delegate to the globa window object, but be easily mockable
 */
@Injectable({
  providedIn: 'root'
})
export class WindowService {
  constructor() {}

  getLocalStorageItem(key: string): string {
    return window.localStorage.getItem(key);
  }

  setLocalStorageItem(key: string, value: string): void {
    window.localStorage.setItem(key, value);
  }

  removeLocalStorageItem(key: string): void {
    window.localStorage.removeItem(key);
  }

  setLocationHref(href: string) {
    window.location.href = href;
  }
}
