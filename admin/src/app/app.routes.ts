import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { IndicatorCategoriesComponent } from './indicator-categories/indicator-categories.component';
import { EditIndicatorCategoryComponent } from './edit-indicator-category/edit-indicator-category.component';
import { IndicatorsComponent } from './indicators/indicators.component';
import { EditIndicatorComponent } from './edit-indicator/edit-indicator.component';
import { AuthenticationComponent } from './authentication/authentication.component';
import { AuthenticationGuard } from './authentication.guard';
import { EcogesturesComponent } from './ecogestures/ecogestures.component';
import { EditEcogestureComponent } from './edit-ecogesture/edit-ecogesture.component';
import { EditPageComponent } from './edit-page/edit-page.component';
import { MediaCategoriesComponent } from './media-categories/media-categories.component';
import { EditMediaCategoryComponent } from './edit-media-category/edit-media-category.component';

export const APP_ROUTES: Routes = [
  { path: 'login', component: AuthenticationComponent },
  {
    path: '',
    canActivate: [AuthenticationGuard],
    children: [
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
        path: 'media-categories',
        children: [
          { path: '', component: MediaCategoriesComponent },
          {
            path: 'new',
            component: EditMediaCategoryComponent
          },
          {
            path: ':media/edit',
            component: EditMediaCategoryComponent
          }
        ]
      },
      {
        path: 'ecogestures',
        children: [
          { path: '', component: EcogesturesComponent },
          {
            path: 'new',
            component: EditEcogestureComponent
          },
          {
            path: ':ecogestureId/edit',
            component: EditEcogestureComponent
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
      },
      {
        path: 'page-models/:modelName/pages',
        children: [
          { path: 'create', component: EditPageComponent },
          { path: ':pageId/edit', component: EditPageComponent }
        ]
      }
    ]
  }
];
