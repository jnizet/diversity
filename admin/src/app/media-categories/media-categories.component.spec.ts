import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ComponentTester, TestButton } from 'ngx-speculoos';
import { EMPTY, of } from 'rxjs';

import { MediaCategoriesComponent } from './media-categories.component';
import { MediaCategoryService } from '../media-category.service';
import { ConfirmationService } from '../confirmation.service';
import { ToastService } from '../toast.service';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { MediaCategory } from '../media-category.model';
import { NgbTestingModule } from '../ngb-testing.module';

class MediaCategoriesComponentTester extends ComponentTester<MediaCategoriesComponent> {
  constructor() {
    super(MediaCategoriesComponent);
  }

  get categories() {
    return this.elements('.category');
  }

  get createLink() {
    return this.element('#create-category');
  }

  get deleteButtons() {
    return this.elements('.delete-category-button') as Array<TestButton>;
  }
}

describe('MediaCategoriesComponent', () => {
  let tester: MediaCategoriesComponentTester;
  let categoryService: jasmine.SpyObj<MediaCategoryService>;
  let confirmationService: jasmine.SpyObj<ConfirmationService>;
  let toastService: jasmine.SpyObj<ToastService>;

  beforeEach(() => {
    categoryService = jasmine.createSpyObj<MediaCategoryService>('MediaCategoryService', ['list', 'delete']);
    confirmationService = jasmine.createSpyObj<ConfirmationService>('ConfirmationService', ['confirm']);
    toastService = jasmine.createSpyObj<ToastService>('ToastService', ['success']);

    TestBed.configureTestingModule({
      imports: [FontAwesomeModule, NgbModalModule, NgbTestingModule, RouterTestingModule],
      declarations: [MediaCategoriesComponent],
      providers: [
        { provide: MediaCategoryService, useValue: categoryService },
        { provide: ConfirmationService, useValue: confirmationService },
        { provide: ToastService, useValue: toastService }
      ]
    });

    tester = new MediaCategoriesComponentTester();
  });

  it('should not display anything until categories are available', () => {
    categoryService.list.and.returnValue(EMPTY);
    tester.detectChanges();

    expect(tester.categories.length).toBe(0);
    expect(tester.createLink).toBeNull();
  });

  it('should display categories', () => {
    const categories: Array<MediaCategory> = [
      {
        id: 1,
        name: 'Category1'
      },
      {
        id: 2,
        name: 'Category2'
      }
    ];

    categoryService.list.and.returnValue(of(categories));
    tester.detectChanges();

    expect(tester.categories.length).toBe(2);
    expect(tester.categories[0]).toContainText('Category1');
    expect(tester.categories[1]).toContainText('Category2');
    expect(tester.createLink).not.toBeNull();
  });

  it('should delete after confirmation and reload', () => {
    const categories: Array<MediaCategory> = [
      {
        id: 1,
        name: 'Category1'
      },
      {
        id: 2,
        name: 'Category2'
      }
    ];

    categoryService.list.and.returnValues(of(categories), of([categories[1]]));
    tester.detectChanges();

    confirmationService.confirm.and.returnValue(of(undefined));
    categoryService.delete.and.returnValue(of(undefined));

    tester.deleteButtons[0].click();

    expect(tester.categories.length).toBe(1);
    expect(categoryService.delete).toHaveBeenCalledWith(1);
    expect(toastService.success).toHaveBeenCalled();
  });
});
