import { Controller } from 'stimulus';
import Swiper, { Navigation, Pagination } from 'swiper';

export class AboutController extends Controller {
  connect() {
    // configure Swiper to use modules
    Swiper.use([Navigation, Pagination]);

    new Swiper('.swiper-partner', {
      speed: 1000,
      keyboard: true,
      slidesPerView: 'auto',
      spaceBetween: 60,
      navigation: {
        prevEl: '.swiper-prev',
        nextEl: '.swiper-next'
      },
      pagination: {
        el: '.swiper-pagination-partner',
        type: 'bullets',
        clickable: true,
        bulletClass: 'bullet-white',
        bulletActiveClass: 'active-white'
      }
    });
  }
}
