import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModalModule, NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { ValdemortModule } from 'ngx-valdemort';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { APP_ROUTES } from './app.routes';
import { IndicatorCategoriesComponent } from './indicator-categories/indicator-categories.component';
import { ConfirmationModalComponent } from './confirmation-modal/confirmation-modal.component';
import { ToastsComponent } from './toasts/toasts.component';
import { ErrorInterceptorService } from './error-interceptor.service';
import { NavbarComponent } from './navbar/navbar.component';
import { EditIndicatorCategoryComponent } from './edit-indicator-category/edit-indicator-category.component';
import { ValidationDefaultsComponent } from './validation-defaults/validation-defaults.component';
import { IndicatorsComponent } from './indicators/indicators.component';
import { EditIndicatorComponent } from './edit-indicator/edit-indicator.component';
import { AuthenticationComponent } from './authentication/authentication.component';
import { AuthenticationInterceptorService } from './authentication-interceptor.service';
import { EcogesturesComponent } from './ecogestures/ecogestures.component';
import { EditEcogestureComponent } from './edit-ecogesture/edit-ecogesture.component';
import { EditPageComponent } from './edit-page/edit-page.component';
import { EditPageElementComponent } from './edit-page-element/edit-page-element.component';
import { EditTextElementComponent } from './edit-text-element/edit-text-element.component';
import { EditLinkElementComponent } from './edit-link-element/edit-link-element.component';
import { EditImageElementComponent } from './edit-image-element/edit-image-element.component';
import { PageLinkComponent } from './home/page-link/page-link.component';
import { HeadingDirective } from './heading/heading.directive';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    IndicatorCategoriesComponent,
    ConfirmationModalComponent,
    ToastsComponent,
    NavbarComponent,
    EditIndicatorCategoryComponent,
    ValidationDefaultsComponent,
    IndicatorsComponent,
    EditIndicatorComponent,
    AuthenticationComponent,
    EcogesturesComponent,
    EditEcogestureComponent,
    EditPageComponent,
    EditPageElementComponent,
    EditTextElementComponent,
    EditLinkElementComponent,
    EditImageElementComponent,
    PageLinkComponent,
    HeadingDirective
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(APP_ROUTES),
    HttpClientModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    NgbModalModule,
    NgbToastModule,
    ValdemortModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptorService, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
