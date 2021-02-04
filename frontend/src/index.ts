import { Application } from 'stimulus';
import { initialize } from './global';
import { ContactController } from './contact/contact-controller';
import { SearchController } from './search/search-controller';
import { IndicatorsController } from './indicators/indicators-controller';
import { HomeController } from './home/home-controller';
import { QuestionItemController } from './indicators/question-item-controller';
import { AboutController } from './about/about-controller';
import { ScienceController } from './science/science-controller';
import { EcogesturesController } from './ecogestures/ecogestures-controller';
import { IndicatorController } from './indicator/indicator-controller';
import { MapController } from './map/map-controller';
import { TerritoryController } from './territory/territory-controller';

const application = Application.start();
application.register('contact', ContactController);
application.register('search', SearchController);
application.register('indicators', IndicatorsController);
application.register('indicator', IndicatorController);
application.register('home', HomeController);
application.register('question-item', QuestionItemController);
application.register('about', AboutController);
application.register('science', ScienceController);
application.register('ecogestures', EcogesturesController);
application.register('map', MapController);
application.register('territory', TerritoryController);

initialize();
