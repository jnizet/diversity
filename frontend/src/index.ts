import { Application } from 'stimulus';
import { ContactController } from './contact/contact-controller';

const application = Application.start();
application.register('contact', ContactController);
