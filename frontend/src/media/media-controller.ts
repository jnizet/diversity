import { Controller } from 'stimulus';
import Swiper from 'swiper';

export class MediaController extends Controller {
  connect() {
    new Swiper('.articles-list', {
      speed: 1000,
      keyboard: true,
      slidesPerView: 2,
      spaceBetween: 60,
      simulateTouch: false,
      navigation: {
        prevEl: '.article-swiper-prev',
        nextEl: '.article-swiper-next'
      }
    });
    new Swiper('.interviews-list', {
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
}
