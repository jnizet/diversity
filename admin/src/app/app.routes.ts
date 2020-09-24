import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { IndicatorCategoriesComponent } from './indicator-categories/indicator-categories.component';
import { EditIndicatorCategoryComponent } from './edit-indicator-category/edit-indicator-category.component';

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
  }
];
