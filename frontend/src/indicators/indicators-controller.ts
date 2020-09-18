import { Controller } from 'stimulus';
import { setElementVisible, showElement } from '../elements';

export class IndicatorsController extends Controller {
  static targets = ['all', 'category', 'indicator'];

  allTarget: HTMLElement;
  categoryTargets: Array<HTMLElement>;
  indicatorTargets: Array<HTMLElement>;

  allClicked() {
    if (this.isCategorySelected(this.allTarget)) {
      return;
    }
    this.categoryTargets.forEach(c => this.setCategorySelected(c, false));
    this.setCategorySelected(this.allTarget, true);

    this.updateIndicatorsVisibility();
  }

  categoryClicked(event: Event) {
    const categoryTarget = event.target as HTMLElement;
    this.setCategorySelected(categoryTarget, !this.isCategorySelected(categoryTarget));

    // if all categories are selected, deselect them and select "all"
    if (this.categoryTargets.every(c => this.isCategorySelected(c))) {
      this.categoryTargets.forEach(c => this.setCategorySelected(c, false));
      this.setCategorySelected(this.allTarget, true);
    } else {
      this.setCategorySelected(this.allTarget, false);
    }

    this.updateIndicatorsVisibility();
  }

  private getCategoryId(categoryTarget: HTMLElement): number {
    return +categoryTarget.dataset.id;
  }

  private getIndicatorCategories(indicatorTarget: HTMLElement): Array<number> {
    return JSON.parse(indicatorTarget.dataset.categories);
  }

  private isCategorySelected(categoryTarget: HTMLElement): boolean {
    return categoryTarget.classList.contains('selected');
  }

  private setCategorySelected(categoryTarget: HTMLElement, selected: boolean): void {
    if (selected) {
      categoryTarget.classList.add('selected');
    } else {
      categoryTarget.classList.remove('selected');
    }
  }

  private updateIndicatorsVisibility() {
    if (this.isCategorySelected(this.allTarget)) {
      this.indicatorTargets.forEach(indicator => showElement(indicator));
    } else {
      const selectedCategoryIds = new Set<number>(
        this.categoryTargets.filter(c => this.isCategorySelected(c)).map(c => this.getCategoryId(c))
      );

      this.indicatorTargets.forEach(indicator => {
        const indicatorCategories = this.getIndicatorCategories(indicator);
        const visible = indicatorCategories.some(categoryId => selectedCategoryIds.has(categoryId));
        setElementVisible(indicator, visible);
      });
    }
  }
}
