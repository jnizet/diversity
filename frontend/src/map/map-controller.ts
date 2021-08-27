import { Controller } from 'stimulus';
import Swiper from 'swiper';
import { TweenMax } from 'gsap';
import $ from 'jquery';

interface MapCoordinates {
  x: number;
  y: number;
}

interface Zone {
  zone: string;
  name: string;
  coordinates: MapCoordinates;
  text: string;
  active: false;
}

interface Territory {
  territory: string;
  name: string;
  slug: string;
  coordinates: MapCoordinates;
  zone: string | null;
  active: boolean;
}

interface MapData {
  zones: Array<Zone>;
  territories: Array<Territory>;
}

export class MapController extends Controller {
  mapData: MapData;

  wh: number;
  ww: number;
  mw: number;
  sm: number;
  introMap = false;
  dest: number;
  ficheSwipersByTerritorySlug = new Map<string, Swiper>();

  async connect() {
    $('.map-container').removeClass('intro');
    $('.territoire-nav').removeClass('closed');
    $('.map-content').removeClass('off');

    const response = await fetch('/map', {
      method: 'GET'
    });
    this.mapData = await response.json();

    this.mapData.territories.forEach((territory, index) => {
      $('.hotspot.hide')
        .clone()
        .removeClass('hide')
        .appendTo('.map-content')
        .css({ top: territory.coordinates.y, left: territory.coordinates.x })
        .addClass('hotspot' + index)
        .addClass(territory.slug);

      let territoryHotspot = $('.hotspot' + index);
      territoryHotspot.find('.hotspot-text').text(territory.name);
      territoryHotspot.data('index', index);
      territoryHotspot.find('.hotspot-outside').css('animation-delay', index / 4 + 's');

      if (territory.zone) {
        territoryHotspot.addClass('not-visible hb');
      }

      if (!territory.active) {
        territoryHotspot.addClass('no-data');
      }
    });

    this.mapData.zones.forEach((zone, index) => {
      $('.bassin.hide')
        .clone()
        .removeClass('hide')
        .appendTo('.map-content')
        .css({ top: zone.coordinates.y, left: zone.coordinates.x })
        .addClass('bassin' + index);
      let zoneHotspot = $('.bassin' + index);
      zoneHotspot.find('.hotspot-text').text(zone.name);
      zoneHotspot.data('index', index);

      if (!zone.active) {
        zoneHotspot.addClass('no-data');
      }
    });

    if (window.matchMedia('(min-width: 1024px)').matches) {
      $('.hotspot, .bassin').on('mouseenter', e => {
        $('.bassin').addClass('hover');
        $('.hotspot').addClass('hover');
        $(e.currentTarget).removeClass('hover');
      });
      $('.hotspot, .bassin').on('mouseleave', () => {
        $('.hotspot').removeClass('hover');
        $('.bassin').removeClass('hover');
      });
    }

    $('.bassin').on('click', e => {
      this.zoomZone($(e.currentTarget).data('index'));
    });

    $('.hotspot:not(.no-data)').on('click', e => {
      this.zoomHotspot($(e.currentTarget).data('index'));
    });

    $('.cta-close-bassin').on('click', () => {
      this.unzoomSafe();
    });

    $('.cta-back-world').on('click', () => {
      this.backWorld();
    });

    $('.nav-slide').on('click', e => {
      let slug = $(e.currentTarget).data('slug');
      this.zoomHotspot(this.mapData.territories.findIndex(t => t.slug === slug));
    });

    if (window.matchMedia('(max-width: 1024px)').matches) {
      let startX: number;
      let memoX = 0;
      let mX = 0;
      this.dest = 0;

      TweenMax.set($('.map-container'), { x: memoX });

      $('.map-mask').on('touchstart', e => {
        var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
        startX = touch.pageX;

        memoX = parseFloat($('.map-container').css('transform').split(',')[4]);

        if (!$('.section-map').hasClass('map-zoom')) {
          TweenMax.set($('.map-container'), { scale: 1, y: 0, x: this.dest });
        }
      });

      $('.map-mask').on('touchmove', e => {
        if (!$('.section-map').hasClass('map-zoom')) {
          const touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
          mX = (startX - touch.pageX) * 2.5;

          this.dest = memoX - mX;

          if (this.dest < -(this.mw - this.ww) / 2) {
            this.dest = -(this.mw - this.ww) / 2;
          }

          if (this.dest > (this.mw - this.ww) / 2) {
            this.dest = (this.mw - this.ww) / 2;
          }

          TweenMax.set($('.map-container'), { x: this.dest, y: 0 });
        }
      });

      $('.map-mask').on('touchend', e => {
        if (!$('.section-map').hasClass('map-zoom')) {
          TweenMax.set($('.map-container'), { scale: 1.05, y: 0, x: this.dest });
        }

        $('.no-data').removeClass('hover');
      });
    }

    this.resizing();
    $(window).on('resize', () => this.resizing());

    $('.swiper-fiche').each((index, element) => {
      const ficheSwiper = new Swiper(element, {
        speed: 1000,
        loop: true,
        grabCursor: true,
        touchRatio: 2,
        simulateTouch: false,
        autoplay: {
          delay: 3000
        },
        pagination: {
          el: '.territoire-fiche-item:nth-child(' + (index + 1) + ') .swiper-pagination-territoire',
          type: 'bullets',
          clickable: true,
          bulletClass: 'bullet-white',
          bulletActiveClass: 'active-yellow'
        }
      });
      this.ficheSwipersByTerritorySlug.set($(element).data('slug'), ficheSwiper);
      ficheSwiper.autoplay.stop();
    });

    // swiper map
    new Swiper('.swiper-fiche-nav', {
      speed: 1000,
      grabCursor: true,
      freeMode: true,
      slidesPerView: 'auto',
      breakpoints: {
        320: {
          direction: 'horizontal',
          allowTouchMove: true
        },

        1024: {
          direction: 'vertical',
          allowTouchMove: $('.nav-slide').length > 5,
          slidesOffsetBefore: 10,
          slidesOffsetAfter: 10
        }
      }
    });
  }

  resizing() {
    this.wh = $(window).innerHeight();
    this.ww = $(window).width();
    this.mw = $('.map-container').width();
    this.sm = $('.section-map').offset().top;
  }

  zoomHotspot(index: number) {
    $('.map-container').removeClass('touch');

    $('.section-map').addClass('map-zoom');
    const territory = this.mapData.territories[index];
    const mapContent = $('.map-content');
    const x = mapContent.width() / 2 - territory.coordinates.x;
    const y = mapContent.height() / 2 - territory.coordinates.y;

    TweenMax.set($('.map-container'), { scale: 3, x: x * 3, y: y * 3, opacity: 0 });

    setTimeout(() => {
      $('.map-wrapper').removeAttr('style');
    }, 2000);

    const delay = $('.nav-img-wrapper.active').length > 0 ? 0 : 1000;

    setTimeout(() => {
      this.openTerritory(territory);
    }, delay);

    $('.nav-img-wrapper').removeClass('active');
    $('.map-content').addClass('off');

    $('.nav-slide[data-slug="' + territory.slug + '"]')
      .find('.nav-img-wrapper')
      .addClass('active');
  }

  zoomZone(index: number) {
    $('.map-container').removeClass('touch');

    $('.section-map').addClass('map-zoom map-zoom-bassin');
    $('.bassin' + index).addClass('zoomin');

    const zone = this.mapData.zones[index];

    this.mapData.territories.forEach((territory, index) => {
      if (territory.zone === zone.zone) {
        $('.hotspot' + index).removeClass('not-visible');
      }
    });

    const mapContent = $('.map-content');
    const x = mapContent.width() / 2 - zone.coordinates.x;
    const y = mapContent.height() / 2 - zone.coordinates.y;

    TweenMax.set($('.map-container'), { scale: 5, x: x * 5, y: y * 5 });

    $('.bassin-text-title').text(zone.name);
    $('.bassin-text-wrapper p').text(zone.text);

    $('.bassin-text-wrapper').delay(1500).fadeIn();
  }

  unzoom() {
    $('.section-map').removeClass('map-zoom map-zoom-bassin');

    $('.zoomin').removeClass('zoomin');

    $('.map-content').addClass('off');
    TweenMax.set($('.map-container'), { scale: 1, x: 0, y: 0 });

    setTimeout(() => {
      $('.map-content').removeClass('off');
    }, 2000);

    this.closeZone();
  }

  unzoomSafe() {
    $('.bassin-text-wrapper').fadeOut();
    $('.map-container').addClass('back_bassin');
    $('.map-content').addClass('off');

    setTimeout(() => {
      $('.section-map').removeClass('map-zoom map-zoom-bassin');
      $('.zoomin').removeClass('zoomin');

      $('.map-container').removeAttr('style').removeClass('back_bassin');
      this.closeZone();
    }, 100);

    setTimeout(() => {
      $('.map-content').removeClass('off');
    }, 2000);

    setTimeout(() => {
      $('.map-container').addClass('touch');
    }, 3000);

    this.dest = 0;
  }

  openTerritory(territory: Territory) {
    const slug = territory.slug;

    $('.territoire-nav').removeClass('closed');
    $('.territoire-fiche-item').removeClass('active');

    $('.territoire-fiche-item[data-slug=' + slug + ']').addClass('active');
    $('.bassin-text-wrapper').fadeOut(400);

    for (let ficheSwiper of this.ficheSwipersByTerritorySlug.values()) {
      ficheSwiper.autoplay.stop();
    }

    this.ficheSwipersByTerritorySlug.get(territory.slug).autoplay.start();
    this.ficheSwipersByTerritorySlug.get(territory.slug).update();
  }

  backWorld() {
    $('.bassin-text-wrapper').fadeOut();

    $('.territoire-fiche-item').removeClass('active');
    $('.section-map').removeClass('map-zoom map-zoom-bassin');
    $('.zoomin').removeClass('zoomin');

    $('.map-wrapper').removeAttr('style');
    $('.map-container').removeAttr('style');

    this.closeZone();

    $('.map-content').removeClass('off');
    $('.nav-img-wrapper').removeClass('active');

    setTimeout(() => {
      $('.map-container').addClass('touch');
    }, 3000);

    this.dest = 0;
  }

  closeZone() {
    this.mapData.territories.forEach((territory, index) => {
      if (territory.zone) {
        $('.hotspot' + index).addClass('not-visible');
      }
    });
  }
}
