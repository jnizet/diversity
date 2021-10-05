import { Controller } from 'stimulus';
import Swiper from 'swiper';
import { setElementVisible, showElement } from '../elements';

export class MediaController extends Controller {
  static targets = ['all', 'category', 'media', 'articles', 'interviews', 'reports'];

  allTarget: HTMLElement;
  categoryTargets: Array<HTMLElement>;
  mediaTargets: Array<HTMLElement>;
  articlesTarget: HTMLElement;
  interviewsTarget: HTMLElement;
  reportsTarget: HTMLElement;

  articlesSwiper: Swiper;
  interviewsSwiper: Swiper;
  reportsSwiper: Swiper;

  isArticlesVisible: boolean;
  isInterviewsVisible: boolean;
  isReportsVisible: boolean;

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
      slidesPerView: 'auto',
      spaceBetween: 60,
      simulateTouch: false,
      navigation: {
        prevEl: '.interview-swiper-prev',
        nextEl: '.interview-swiper-next'
      }
    });
    this.reportsSwiper = new Swiper('.reports-list', {
      speed: 1000,
      keyboard: true,
      slidesPerView: 'auto',
      spaceBetween: 60,
      simulateTouch: false,
      navigation: {
        prevEl: '.reports-swiper-prev',
        nextEl: '.reports-swiper-next'
      }
    });
  }

  allClicked() {
    if (this.isCategorySelected(this.allTarget)) {
      return;
    }
    this.categoryTargets.forEach(c => this.setCategorySelected(c, false));
    this.setCategorySelected(this.allTarget, true);

    this.updateMediasVisibility();
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

    this.updateMediasVisibility();
  }

  private getCategoryId(categoryTarget: HTMLElement): number {
    return +categoryTarget.dataset.id;
  }

  private getMediaCategories(mediaTarget: HTMLElement): Array<number> {
    return JSON.parse(mediaTarget.dataset.categories);
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

  private updateSectionVisibilty(mediaTarget: HTMLElement, visible: boolean): void {
    switch (mediaTarget.dataset.type) {
      case 'ARTICLE':
        this.isArticlesVisible = this.isArticlesVisible || visible;
        break;
      case 'INTERVIEW':
        this.isInterviewsVisible = this.isInterviewsVisible || visible;
        break;
      case 'REPORT':
        this.isReportsVisible = this.isReportsVisible || visible;
        break;
    }
  }

  private updateMediasVisibility() {
    this.isArticlesVisible = false;
    this.isInterviewsVisible = false;
    this.isReportsVisible = false;

    if (this.isCategorySelected(this.allTarget)) {
      this.mediaTargets.forEach(media => showElement(media));
      this.isArticlesVisible = true;
      this.isInterviewsVisible = true;
      this.isReportsVisible = true;
    } else {
      const selectedCategoryIds = new Set<number>(
        this.categoryTargets.filter(c => this.isCategorySelected(c)).map(c => this.getCategoryId(c))
      );

      this.mediaTargets.forEach(media => {
        const mediaCategories = this.getMediaCategories(media);
        const visible = mediaCategories.some(categoryId => selectedCategoryIds.has(categoryId));
        setElementVisible(media, visible);
        this.updateSectionVisibilty(media, visible);
      });
    }

    setElementVisible(this.articlesTarget, this.isArticlesVisible);
    setElementVisible(this.interviewsTarget, this.isInterviewsVisible);
    setElementVisible(this.reportsTarget, this.isReportsVisible);

    this.interviewsSwiper.update();
    this.articlesSwiper.update();
    this.reportsSwiper.update();
  }
}
