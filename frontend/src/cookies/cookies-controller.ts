import { Controller } from 'stimulus';
import { hideElement, showElement } from '../elements';

const KEY = 'cookies-consent';

export class CookiesController extends Controller {
  static targets = ['warning'];

  warningTarget: HTMLElement;

  connect() {
    const item = window.localStorage.getItem(KEY);
    if (!item) {
      showElement(this.warningTarget);
    }
  }

  accept() {
    hideElement(this.warningTarget);
    window.localStorage.setItem(KEY, '1');
  }
}
