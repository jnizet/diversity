import { Controller } from 'stimulus';
import { isElementVisible, setElementVisible } from '../elements';

export class SearchController extends Controller {
  static targets = ['searchInput'];

  searchInputTarget: HTMLInputElement;

  toggle(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    const newVisible = !isElementVisible(this.searchInputTarget);
    setElementVisible(this.searchInputTarget, newVisible);
    if (newVisible) {
      this.searchInputTarget.focus();
      this.searchInputTarget.select();
    }
  }
}
