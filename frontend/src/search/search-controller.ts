import { Controller } from '@hotwired/stimulus';
import { hideElement, showElement } from '../elements';

export class SearchController extends Controller {
  static targets = ['searchInput', 'popin'];

  searchInputTarget: HTMLInputElement;
  popinTarget: HTMLDivElement;

  open(event: Event) {
    event.stopPropagation();
    showElement(this.popinTarget);
    this.searchInputTarget.focus();
  }

  close(event: Event) {
    event.stopPropagation();
    hideElement(this.popinTarget);
  }
}
