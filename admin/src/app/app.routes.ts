import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { IndicatorCategoriesComponent } from './indicator-categories/indicator-categories.component';
import { EditIndicatorCategoryComponent } from './edit-indicator-category/edit-indicator-category.component';
import { IndicatorsComponent } from './indicators/indicators.component';
import { EditIndicatorComponent } from './edit-indicator/edit-indicator.component';

export const APP_ROUTES: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'indicator-categories',
    children: [
      { path: '', component: IndicatorCategoriesComponent },
      {
        path: 'new',
        component: EditIndicatorCategoryComponent
      },
      {
        path: ':indicatorCategoryId/edit',
        component: EditIndicatorCategoryComponent
      }
    ]
  },
  {
    path: 'indicators',
    children: [
      { path: '', component: IndicatorsComponent },
      {
        path: 'new',
        component: EditIndicatorComponent
      },
      {
        path: ':indicatorId/edit',
        component: EditIndicatorComponent
      }
    ]
  }
];
