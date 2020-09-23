import { TweenMax, TweenLite } from 'gsap';
import $ from 'jquery';

export function initialize() {
  $(document).ready(function () {
    let memo_scroll: number, wh: number, ww: number, nv: number;
    let deco_y: Array<any> = [];

    resizing();
    $('.section-top').css('height', wh);

    scrolling();

    $(document).scroll(scrolling);
    $(window).resize(resizing);

    function resizing() {
      wh = $(window).innerHeight();
      ww = $(window).width();
      nv = $('.nav').height();

      $('.section-decoration-text').each(function (index) {
        deco_y[index] = -$(this).offset().top + wh / 2 - $(this).height() / 2;
      });
    }

    function scrolling() {
      var scroll = $(document).scrollTop();

      /// navbar ///

      if (window.matchMedia('(min-width: 480px)').matches) {
        //desktop

        if (scroll > memo_scroll) {
          $('.header').addClass('closed');
        } else {
          $('.header').removeClass('closed');

          if (scroll <= 0) {
            $('.header').removeClass('small');
          } else {
            $('.header').addClass('small');
          }

          if (scroll <= ww / 2 - nv) {
            $('.nav').removeClass('sticky');
          } else {
            $('.nav').addClass('sticky');
          }
        }
      } else {
        //mobile

        if (scroll > 0) {
          $('.nav').addClass('sticky');
        } else {
          $('.nav').removeClass('sticky');
        }
      }
      memo_scroll = scroll;

      /// decoration text ///

      $('.section-decoration-text').each(function (index) {
        var t = scroll + deco_y[index];
        TweenMax.set(this, { x: t / 10 });
      });
    }

    $('.btn-menu').on('click', function () {
      if ($('body').hasClass('menu-open')) {
        close_menu();
      } else {
        open_menu();
      }
    });

    $('.overlay').on('click', close_menu);

    function open_menu() {
      $('body').addClass('menu-open');
      $('.overlay').addClass('open');
      $('.nav-link-list').addClass('open');

      TweenLite.to('.l1', 0.25, { attr: { x1: 3, y1: 3, x2: 19, y2: 19 }, ease: 'cache', overwrite: 'all' });
      TweenLite.to('.l3', 0.25, { attr: { x1: 3, y1: 19, x2: 19, y2: 3 }, ease: 'cache', overwrite: 'all' });
      TweenLite.to('.l2', 0, { opacity: 0, ease: 'cache', overwrite: 'all' });
    }

    function close_menu() {
      $('body').removeClass('menu-open');
      $('.overlay').removeClass('open');
      $('.nav-link-list').removeClass('open');

      TweenLite.to('.l1', 0.25, { attr: { x1: 1, y1: 3, x2: 21, y2: 3 }, ease: 'cache', overwrite: 'all' });
      TweenLite.to('.l3', 0.25, { attr: { x1: 1, y1: 19, x2: 10, y2: 19 }, ease: 'cache', overwrite: 'all' });
      TweenLite.to('.l2', 0.25, { opacity: 1, ease: 'cache', overwrite: 'all' });
    }

    setInterval(anim_menu, 5000);

    function anim_menu() {
      if (!$('body').hasClass('menu-open')) {
        TweenMax.to($('.l3'), 0.5, { xPercent: 120 });
        TweenMax.to($('.l3'), 0.5, { xPercent: 0, delay: 0.5 });
      }
    }

    $('.apprendre-top').on('click', function () {
      if (!$(this).parent().hasClass('closed')) {
        $(this).parent().addClass('closed');
        $(this).parent().find('.apprendre-bottom').removeAttr('style');
      } else {
        $(this).parent().removeClass('closed');

        $(this)
          .parent()
          .find('.apprendre-bottom')
          .css('height', $(this).parent().find('.apprendre-bottom').prop('scrollHeight') + 20);
      }
    });
  });
}
