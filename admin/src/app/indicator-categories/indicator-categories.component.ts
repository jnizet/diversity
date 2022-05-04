import { Component, OnInit } from '@angular/core';
import { faPlus, faTag, faTrash } from '@fortawesome/free-solid-svg-icons';

import { IndicatorCategory } from '../indicator-category.model';
import { IndicatorCategoryService } from '../indicator-category.service';
import { ConfirmationService } from '../confirmation.service';
import { ToastService } from '../toast.service';
import { switchMap, tap } from 'rxjs';

@Component({
  selector: 'biom-categories',
  templateUrl: './indicator-categories.component.html',
  styleUrls: ['./indicator-categories.component.scss']
})
export class IndicatorCategoriesComponent implements OnInit {
  categories: Array<IndicatorCategory>;

  categoryIcon = faTag;
  createCategoryIcon = faPlus;
  deleteCategoryIcon = faTrash;

  constructor(
    private indicatorCategoryService: IndicatorCategoryService,
    private confirmationService: ConfirmationService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.indicatorCategoryService.list().subscribe(categories => (this.categories = categories));
  }

  deleteCategory(category: IndicatorCategory) {
    this.confirmationService
      .confirm({ message: 'Voulez-vous vraiment supprimer cette catégorie\u00a0?' })
      .pipe(
        switchMap(() => this.indicatorCategoryService.delete(category.id)),
        tap(() => this.toastService.success(`La catégorie ${category.name} a été supprimée`)),
        switchMap(() => this.indicatorCategoryService.list())
      )
      .subscribe(categories => (this.categories = categories));
  }
}
