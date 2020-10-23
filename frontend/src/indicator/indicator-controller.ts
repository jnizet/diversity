import { Controller } from 'stimulus';
import Swiper, { Navigation, Pagination } from 'swiper';

export class IndicatorController extends Controller {
  connect() {
    // configure Swiper to use modules
    Swiper.use([Navigation, Pagination]);

    new Swiper('.swiper-indicateur', {
      speed: 1000,
      keyboard: true,
      slidesPerView: 'auto',
      spaceBetween: 60,
      watchOverflow: true,
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

  // TODO deal with bassins when it's ready
}
