import $ from 'jquery';
import { Controller } from '@hotwired/stimulus';
import Swiper from 'swiper';

export class IndicatorController extends Controller {
  static targets = ['modal'];

  modalTarget: HTMLElement;

  connect() {
    new Swiper('.swiper-indicateur', {
      speed: 1000,
      keyboard: true,
      slidesPerView: 'auto',
      spaceBetween: 60,
      watchOverflow: true,
      simulateTouch: false,
      navigation: {
        prevEl: '.swiper-prev',
        nextEl: '.swiper-next'
      },
      pagination: {
        el: '.swiper-pagination-indicateur',
        type: 'bullets',
        clickable: true,
        bulletClass: 'bullet-white',
        bulletActiveClass: 'active-white'
      }
    });
  }

  closeModal() {
    $('.visual-modal').removeClass('visual-modal-visible');
    setTimeout(() => $('.visual-modal-container').removeClass('visual-modal-container-visible'), 100);
  }

  openModal() {
    $('.visual-modal').addClass('visual-modal-visible');
    setTimeout(() => $('.visual-modal-container').addClass('visual-modal-container-visible'), 100);
  }
  clickVisual(event: Event) {
    event.stopPropagation();
    event.preventDefault();
  }
  // TODO deal with bassins when it's worth it
}
