import * as jQuery from 'jquery';
import { Application } from 'stimulus';
import { ContactController } from './contact/contact-controller';
import { SearchController } from './search/search-controller';
import { CookiesController } from './cookies/cookies-controller';
import { IndicatorsController } from './indicators/indicators-controller';
import { HomeController } from './home/home-controller';
import { initialize } from './global';

(window as any).$ = (window as any).jQuery = jQuery;

const application = Application.start();
application.register('contact', ContactController);
application.register('search', SearchController);
application.register('cookies', CookiesController);
application.register('indicators', IndicatorsController);
application.register('home', HomeController);

initialize();
