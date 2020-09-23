import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbToastModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { APP_ROUTES } from './app.routes';
import { IndicatorCategoriesComponent } from './indicator-categories/indicator-categories.component';
import { ConfirmationModalComponent } from './confirmation-modal/confirmation-modal.component';
import { ToastsComponent } from './toasts/toasts.component';
import { ErrorInterceptorService } from './error-interceptor.service';
import { NavbarComponent } from './navbar/navbar.component';

@NgModule({
  declarations: [AppComponent, HomeComponent, IndicatorCategoriesComponent, ConfirmationModalComponent, ToastsComponent, NavbarComponent],
  imports: [BrowserModule, RouterModule.forRoot(APP_ROUTES), HttpClientModule, FontAwesomeModule, NgbToastModule],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule {}
