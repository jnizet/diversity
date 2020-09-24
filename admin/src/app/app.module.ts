import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
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
import { EditIndicatorCategoryComponent } from './edit-indicator-category/edit-indicator-category.component';
import { ValidationDefaultsComponent } from './validation-defaults/validation-defaults.component';
import { ValdemortModule } from 'ngx-valdemort';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    IndicatorCategoriesComponent,
    ConfirmationModalComponent,
    ToastsComponent,
    NavbarComponent,
    EditIndicatorCategoryComponent,
    ValidationDefaultsComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(APP_ROUTES),
    HttpClientModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    NgbToastModule,
    ValdemortModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule {}
