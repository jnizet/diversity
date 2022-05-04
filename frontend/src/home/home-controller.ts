import { Controller } from '@hotwired/stimulus';
import Swiper from 'swiper';
import $ from 'jquery';

export class HomeController extends Controller {
  connect() {
    new Swiper('.swiper-top', {
      speed: 1000,
      effect: 'fade',
      simulateTouch: false,
      fadeEffect: {
        crossFade: true
      },
      loop: true,
      keyboard: true,
      navigation: {
        nextEl: '.section-top .swiper-next'
      },
      pagination: {
        el: '.section-top .swiper-pagination',
        type: 'bullets',
        clickable: true,
        bulletClass: 'bullet',
        bulletActiveClass: 'active'
      },
      autoplay: {
        delay: 10000,
        disableOnInteraction: false
      },
      on: {
        transitionStart: function (this: any) {
          $('.timer').removeClass('full');
        },
        transitionEnd: function (this: any) {
          $('.timer').addClass('full');
        }
      }
    });
    $('.img-full').eq(0).addClass('anim-zoom');

    new Swiper('.swiper-apropos', {
      navigation: {
        nextEl: '.section-apropos .swiper-next-black'
      },
      pagination: {
        el: '.section-apropos .swiper-pagination-black',
        type: 'bullets',
        clickable: true,
        bulletClass: 'bullet-black',
        bulletActiveClass: 'active'
      },
      breakpoints: {
        // when window width is >= 320px
        320: {
          slidesPerView: 1,
          slidesOffsetAfter: 120,
          touchRatio: 2,
          allowTouchMove: true,
          loop: true
        },
        // when window width is >= 480px
        600: {
          spaceBetween: 60,
          slidesPerColumnFill: 'row',
          slidesPerView: 2,
          slidesPerColumn: 2,
          allowTouchMove: false,
          loop: false
        }
      }
    });
  }
}
