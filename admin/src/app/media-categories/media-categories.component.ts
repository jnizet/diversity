import { Component, OnInit } from '@angular/core';
import { faPlus, faTag, faTrash } from '@fortawesome/free-solid-svg-icons';

import { MediaCategory } from '../media-category.model';
import { MediaCategoryService } from '../media-category.service';
import { ConfirmationService } from '../confirmation.service';
import { ToastService } from '../toast.service';
import { switchMap, tap } from 'rxjs/operators';

@Component({
  selector: 'biom-categories',
  templateUrl: './media-categories.component.html',
  styleUrls: ['./media-categories.component.scss']
})
export class MediaCategoriesComponent implements OnInit {
  categories: Array<MediaCategory>;

  categoryIcon = faTag;
  createCategoryIcon = faPlus;
  deleteCategoryIcon = faTrash;

  constructor(
    private mediaCategoryService: MediaCategoryService,
    private confirmationService: ConfirmationService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.mediaCategoryService.list().subscribe(categories => (this.categories = categories));
  }

  deleteCategory(category: MediaCategory) {
    this.confirmationService
      .confirm({ message: 'Voulez-vous vraiment supprimer cette catégorie\u00a0?' })
      .pipe(
        switchMap(() => this.mediaCategoryService.delete(category.id)),
        tap(() => this.toastService.success(`La catégorie ${category.name} a été supprimée`)),
        switchMap(() => this.mediaCategoryService.list())
      )
      .subscribe(categories => (this.categories = categories));
  }
}
