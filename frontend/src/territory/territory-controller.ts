import { Controller } from '@hotwired/stimulus';
import Swiper from 'swiper';
import $ from 'jquery';

export class TerritoryController extends Controller {
  static targets = ['modal'];

  modalTarget: HTMLElement;

  connect() {
    const locationSwiper = new Swiper('.swiper-lieu', {
      speed: 1000,
      slidesPerView: 'auto',
      freeMode: true,
      grabCursor: true,
      touchRatio: 2,
      navigation: {
        nextEl: '.swiper-navigation-wide .swiper-next',
        prevEl: '.swiper-navigation-wide .swiper-prev'
      },
      breakpoints: {
        320: {
          slidesOffsetBefore: 0
        },
        480: {
          slidesOffsetBefore: 120
        }
      }
    });

    const checkImageInterval = setInterval(() => {
      checkImage();
      locationSwiper.update();
    }, 100);

    checkImage();

    function checkImage() {
      $('.lieu-slide').each((index, element) => {
        const $element = $(element);
        const $image = $element.find('.lieu-slide-img');
        if ($image.width() > $image.height()) {
          if (
            index > 0 &&
            $('.lieu-slide')
              .eq(index - 1)
              .hasClass('type1')
          ) {
            $element.addClass('type2');
          } else {
            $element.addClass('type1');
          }
        } else {
          $element.addClass('type3');
        }
      });

      let imageCount = 0;
      const $localionImageSlides = $('.lieu-slide-img');
      $localionImageSlides.each((index, element) => {
        if (($(element).get(0) as HTMLImageElement).naturalHeight > 0) {
          imageCount++;
        }
      });

      if (imageCount >= $localionImageSlides.length) {
        clearInterval(checkImageInterval);
      }
    }

    new Swiper('.swiper-frise', {
      speed: 1000,
      slidesPerView: 'auto',
      slidesOffsetAfter: 60,
      freeMode: true,
      freeModeMomentum: true,
      grabCursor: true,
      touchRatio: 2
    });

    new Swiper('.swiper-espece', {
      speed: 1000,
      effect: 'fade',
      simulateTouch: false,
      fadeEffect: {
        crossFade: true
      },
      loop: true,
      navigation: {
        nextEl: '.section-espece .swiper-next',
        prevEl: '.section-espece .swiper-prev'
      },
      pagination: {
        el: '.swiper-pagination-espece',
        type: 'bullets',
        clickable: true,
        bulletClass: 'bullet-white',
        bulletActiveClass: 'active-white'
      }
    });

    new Swiper('.swiper-ecosysteme', {
      speed: 1000,
      effect: 'fade',
      simulateTouch: false,
      fadeEffect: {
        crossFade: true
      },
      loop: true,
      navigation: {
        nextEl: '.section-ecosysteme .swiper-next',
        prevEl: '.section-ecosysteme .swiper-prev'
      },
      pagination: {
        el: '.swiper-pagination-eco',
        type: 'bullets',
        clickable: true,
        bulletClass: 'bullet-black',
        bulletActiveClass: 'active'
      }
    });
  }

  closeModal() {
    $('.zoom-modal').removeClass('zoom-modal-visible');
    setTimeout(() => $('.zoom-modal-container').removeClass('zoom-modal-container-visible'), 100);
  }

  openModal() {
    $('.zoom-modal').addClass('zoom-modal-visible');
    setTimeout(() => $('.zoom-modal-container').addClass('zoom-modal-container-visible'), 100);
  }

  clickMap(event: Event) {
    event.stopPropagation();
    event.preventDefault();
  }
}
