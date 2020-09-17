import { Application } from 'stimulus';
import { ContactController } from './contact/contact-controller';
import { SearchController } from './search/search-controller';
import { CookiesController } from './cookies/cookies-controller';

const application = Application.start();
application.register('contact', ContactController);
application.register('search', SearchController);
application.register('cookies', CookiesController);
