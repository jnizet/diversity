import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { IndicatorCategoriesComponent } from './indicator-categories/indicator-categories.component';

export const APP_ROUTES: Routes = [
  { path: '', component: HomeComponent },
  { path: 'indicator-categories', component: IndicatorCategoriesComponent }
];
