/* global $ */



var swiper_top = new Swiper('.swiper-top', {
    delay: 2000,
    cssMode: true,
    loop: true,
    keyboard: true,
    grabCursor: true,
    effect: "fade",
    navigation: {
        nextEl: '.section-top .swiper-next',
        prevEl: '.section-top .swiper-prev',
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
        disableOnInteraction:false
    },
    on: {
        transitionStart: function() {
            $('.timer').removeClass('full');
            $('.swiper-top .img-full').eq(this.activeIndex).addClass('anim-zoom').removeClass('anim-paused');;
        },
        transitionEnd: function() {
            $('.timer').addClass('full');
            $('.swiper-top .img-full').eq(this.previousIndex).addClass('anim-paused');
        },
    }

});




$('.img-full').eq(0).addClass('anim-zoom');
