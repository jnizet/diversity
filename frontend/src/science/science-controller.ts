import { Controller } from '@hotwired/stimulus';
import Swiper from 'swiper';
import $ from 'jquery';

export class ScienceController extends Controller {
  connect() {
    const topSwiper = new Swiper('.swiper-science', {
      speed: 1000,
      keyboard: true,
      effect: 'fade',
      freeMode: true,
      loop: true,
      simulateTouch: false,
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

    topSwiper.on('slideChange', function () {
      if (window.matchMedia('(max-width: 990px)').matches) {
        const body = $('html, body');
        body.stop().animate({ scrollTop: $('.section-science-exemple').offset().top - 60 }, 500, 'swing');
      }
    });
  }
}
