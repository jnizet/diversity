import { Controller } from 'stimulus';
import Swiper, { Navigation, Pagination, EffectFade } from 'swiper';

export class ScienceController extends Controller {
  connect() {
    // configure Swiper to use modules
    Swiper.use([Navigation, Pagination, EffectFade]);

    new Swiper('.swiper-science', {
      speed: 1000,
      keyboard: true,
      effect: 'fade',
      loop: true,
      fadeEffect: {
        crossFade: true
      },
      navigation: {
        nextEl: '.swiper-next'
      },
      pagination: {
        el: '.swiper-pagination-science',
        type: 'bullets',
        clickable: true,
        bulletClass: 'bullet-white',
        bulletActiveClass: 'active-white'
      }
    });
  }
}
