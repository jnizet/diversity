import { Controller } from 'stimulus';
import Swiper from 'swiper';
import { setElementVisible, showElement } from '../elements';

export class MediaController extends Controller {
  static targets = ['all', 'category', 'media'];

  allTarget: HTMLElement;
  categoryTargets: Array<HTMLElement>;
  mediaTargets: Array<HTMLElement>;

  articlesSwiper: Swiper;
  interviewsSwiper: Swiper;

  connect() {
    this.articlesSwiper = new Swiper('.articles-list', {
      speed: 1000,
      keyboard: true,
      slidesPerView: 'auto',
      simulateTouch: false,
      navigation: {
        prevEl: '.article-swiper-prev',
        nextEl: '.article-swiper-next'
      }
    });
    this.interviewsSwiper = new Swiper('.interviews-list', {
      speed: 1000,
      keyboard: true,
      slidesPerView: 2,
      spaceBetween: 60,
      simulateTouch: false,
      navigation: {
        prevEl: '.interview-swiper-prev',
        nextEl: '.interview-swiper-next'
      }
    });
  }

  allClicked() {
    if (this.isCategorySelected(this.allTarget)) {
      return;
    }
    this.categoryTargets.forEach(c => this.setCategorySelected(c, false));
    this.setCategorySelected(this.allTarget, true);

    this.updateIndicatorsVisibility();
  }

  categoryClicked(event: Event) {
    event.preventDefault();
    const categoryTarget = event.target as HTMLElement;
    this.setCategorySelected(categoryTarget, !this.isCategorySelected(categoryTarget));

    // if no category is selected, select "all"
    if (!this.categoryTargets.find(c => this.isCategorySelected(c))) {
      this.setCategorySelected(this.allTarget, true);
    }
    // if all categories are selected, deselect them and select "all"
    else if (this.categoryTargets.every(c => this.isCategorySelected(c))) {
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
    return categoryTarget.classList.contains('active');
  }

  private setCategorySelected(categoryTarget: HTMLElement, selected: boolean): void {
    if (selected) {
      categoryTarget.classList.add('active');
    } else {
      categoryTarget.classList.remove('active');
    }
  }

  private updateIndicatorsVisibility() {
    if (this.isCategorySelected(this.allTarget)) {
      this.mediaTargets.forEach(media => showElement(media));
    } else {
      const selectedCategoryIds = new Set<number>(
        this.categoryTargets.filter(c => this.isCategorySelected(c)).map(c => this.getCategoryId(c))
      );

      this.mediaTargets.forEach(media => {
        const mediaCategories = this.getIndicatorCategories(media);
        const visible = mediaCategories.some(categoryId => selectedCategoryIds.has(categoryId));
        setElementVisible(media, visible);
      });
    }
    this.interviewsSwiper.update();
    this.articlesSwiper.update();
  }
}
