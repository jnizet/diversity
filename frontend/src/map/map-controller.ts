import { Controller } from 'stimulus';
import Swiper, { Navigation, Pagination } from 'swiper';
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

  async connect() {
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
        .addClass('hotspot' + index);

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

    $('.hotspot, .bassin').on('mouseenter', e => {
      $('.bassin').addClass('hover');
      $('.hotspot').addClass('hover');
      $(e.currentTarget).removeClass('hover');
    });
    $('.hotspot, .bassin').on('mouseleave', () => {
      $('.hotspot').removeClass('hover');
      $('.bassin').removeClass('hover');
    });

    $('.bassin').on('click', e => {
      this.zoomZone($(e.currentTarget).data('index'));
    });

    $('.hotspot').on('click', e => {
      this.zoomHotspot($(e.currentTarget).data('index'));
    });

    $('.cta-close-bassin').on('click', () => {
      this.unzoom();
    });

    $('.cta-back-world').on('click', () => {
      this.backWorld();
    });

    $('.nav-slide').on('click', e => {
      let slug = $(e.currentTarget).data('slug');
      this.zoomHotspot(this.mapData.territories.findIndex(t => t.slug === slug));
    });

    // configure Swiper to use modules
    Swiper.use([Navigation, Pagination]);
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

  zoomHotspot(index: number) {
    $('.section-map').addClass('map-zoom');
    const territory = this.mapData.territories[index];
    const mapContent = $('.map-content');
    const x = mapContent.width() / 2 - territory.coordinates.x;
    const y = mapContent.height() / 2 - territory.coordinates.y;

    TweenMax.set($('.map-wrapper'), { x, y });
    TweenMax.set($('.map-container'), { scale: 5, opacity: 0 });

    this.openTerritory(territory);
  }

  zoomZone(index: number) {
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

    TweenMax.set($('.map-wrapper'), { x, y });
    TweenMax.set($('.map-container'), { scale: 5 });

    $('.bassin-text-wrapper[data-slug=' + zone.zone + ']')
      .delay(1500)
      .fadeIn();
  }

  unzoom() {
    $('.bassin-text-wrapper').fadeOut(200);
    $('.section-map').removeClass('map-zoom map-zoom-bassin').addClass('transition-back');

    TweenMax.to($('.map-wrapper'), 1, { x: 0, y: 0 });
    (TweenMax as any).fromTo(
      $('.map-container'),
      { scale: 2 },
      {
        scale: 1,
        duration: 1,
        opacity: 1,
        onComplete: () => {
          $('.section-map').removeClass('transition-back');
        }
      }
    );

    $('.zoomin').removeClass('zoomin');

    this.closeZone();
  }

  openTerritory(territory: Territory) {
    const slug = territory.slug;

    $('.territoire-nav').removeClass('closed');
    $('.territoire-fiche-item').removeClass('active');

    $('.territoire-fiche-item[data-slug=' + slug + ']').addClass('active');
    $('.bassin-text-wrapper').fadeOut(400);

    $('.swiper-fiche').each(function () {
      new Swiper(this, {
        speed: 1000,
        loop: true,
        grabCursor: true,
        autoplay: {
          delay: 4000
        },
        pagination: {
          el: '.swiper-pagination-territoire',
          type: 'bullets',
          clickable: true,
          bulletClass: 'bullet-white',
          bulletActiveClass: 'active-yellow'
        }
      });
    });
  }

  backWorld() {
    $('.territoire-fiche-item').removeClass('active');
    $('.section-map').removeClass('map-zoom map-zoom-bassin');
    $('.zoomin').removeClass('zoomin');
    $('.map-wrapper').removeAttr('style');
    $('.map-container').removeClass('hide');

    (TweenMax as any).fromTo($('.map-container'), { scale: 2, opacity: 0 }, { scale: 1, duration: 1, opacity: 1, delay: 0.4 });
    this.closeZone();

    $('.territoire-nav').addClass('closed');
  }

  closeZone() {
    this.mapData.territories.forEach((territory, index) => {
      if (territory.zone) {
        $('.hotspot' + index).addClass('not-visible');
      }
    });
  }
}
