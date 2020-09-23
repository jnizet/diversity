import { Controller } from 'stimulus';
import Swiper, { SwiperOptions } from 'swiper';

export class HomeController extends Controller {
  connect() {
    new Swiper('.swiper-top', {
      delay: 2000, // weird: this option is not recognized by the typing. Fixed by type assertion `as S``iperOptions
      cssMode: true,
      loop: true,
      keyboard: true,
      grabCursor: true,
      effect: 'fade',
      navigation: {
        nextEl: '.section-top .swiper-next',
        prevEl: '.section-top .swiper-prev'
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
          $('.swiper-top .img-full').eq(this.activeIndex).addClass('anim-zoom').removeClass('anim-paused');
        },
        transitionEnd: function (this: any) {
          $('.timer').addClass('full');
          $('.swiper-top .img-full').eq(this.previousIndex).addClass('anim-paused');
        }
      }
    } as SwiperOptions);

    $('.img-full').eq(0).addClass('anim-zoom');
  }
}
