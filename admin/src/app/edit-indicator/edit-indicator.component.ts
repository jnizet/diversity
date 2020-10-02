import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';

import { Indicator, IndicatorCommand, IndicatorValue } from '../indicator.model';
import { IndicatorService } from '../indicator.service';
import { ToastService } from '../toast.service';
import { IndicatorCategory } from '../indicator-category.model';
import { finalize } from 'rxjs/operators';
import { faMinusCircle, faPlusCircle, faSpinner } from '@fortawesome/free-solid-svg-icons';
import { IndicatorCategoryService } from '../indicator-category.service';
import { SLUG_REGEX } from '../validators';

interface FormValue {
  slug: string;
  biomId: string;
  categoryIds: Array<number>;
}

@Component({
  selector: 'biom-edit-indicator',
  templateUrl: './edit-indicator.component.html',
  styleUrls: ['./edit-indicator.component.scss']
})
export class EditIndicatorComponent implements OnInit {
  mode: 'create' | 'update' = 'create';
  editedIndicator: Indicator;
  indicatorValues: Array<IndicatorValue>;
  categories: Array<IndicatorCategory>;
  isFetchingValues = false;
  noData = false;
  spinnerIcon = faSpinner;
  addIcon = faPlusCircle;
  removeIcon = faMinusCircle;
  categoryIds: FormArray;
  form: FormGroup;

  constructor(
    private route: ActivatedRoute,
    fb: FormBuilder,
    private indicatorService: IndicatorService,
    private indicatorCategoryService: IndicatorCategoryService,
    private router: Router,
    private toastService: ToastService
  ) {
    this.categoryIds = fb.array(
      [null],
      // we need at least one category
      array => (array.value.filter((id: number) => id).length === 0 ? { required: true } : null)
    );
    this.form = fb.group({
      slug: ['', [Validators.required, Validators.pattern(SLUG_REGEX)]],
      biomId: ['', [Validators.required, Validators.pattern(SLUG_REGEX)]],
      categoryIds: this.categoryIds
    });
  }

  ngOnInit(): void {
    this.indicatorCategoryService.list().subscribe(categories => (this.categories = categories));
    const indicatorId = this.route.snapshot.paramMap.get('indicatorId');
    if (indicatorId) {
      this.mode = 'update';
      this.indicatorService.get(+indicatorId).subscribe(indicator => {
        this.editedIndicator = indicator;
        const formValue: Partial<FormValue> = {
          slug: indicator.slug,
          biomId: indicator.biomId
        };
        this.form.patchValue(formValue);
        this.categoryIds.clear();
        indicator.categories.forEach(category => this.categoryIds.push(new FormControl(category.id)));
      });
    }
  }

  unselectedCategories(currentCategory: number) {
    // keep the current category and the other unselected ones
    return this.categories
      ? this.categories.filter(cat => cat.id === currentCategory || !(this.form.value as FormValue).categoryIds.includes(cat.id))
      : [];
  }

  addCategory() {
    this.categoryIds.push(new FormControl(null));
  }

  removeCategory(index: number) {
    this.categoryIds.removeAt(index);
  }

  save() {
    if (this.form.invalid) {
      return;
    }

    const formValue: FormValue = this.form.value;
    const command: IndicatorCommand = {
      slug: formValue.slug,
      biomId: formValue.biomId,
      categoryIds: formValue.categoryIds.filter(id => id)
    };

    let obs: Observable<Indicator | void>;
    if (this.mode === 'update') {
      obs = this.indicatorService.update(this.editedIndicator.id, command);
    } else {
      obs = this.indicatorService.create(command);
    }

    obs.subscribe(() => {
      this.router.navigate(['/indicators']);
      this.toastService.success(`L'indicateur a été ${this.mode === 'update' ? 'modifié' : 'créé'}`);
    });
  }

  getValues() {
    this.isFetchingValues = true;
    this.noData = false;
    const biomId = (this.form.value as FormValue).biomId;
    this.indicatorService
      .getValues(biomId)
      .pipe(finalize(() => (this.isFetchingValues = false)))
      .subscribe({
        next: values => (this.indicatorValues = values),
        error: () => (this.noData = true)
      });
  }
}
