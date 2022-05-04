import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { finalize, Observable } from 'rxjs';

import { Indicator, IndicatorCommand, ValuedIndicator } from '../indicator.model';
import { IndicatorService } from '../indicator.service';
import { ToastService } from '../toast.service';
import { IndicatorCategory } from '../indicator-category.model';
import { faMinusCircle, faPlusCircle, faSpinner } from '@fortawesome/free-solid-svg-icons';
import { IndicatorCategoryService } from '../indicator-category.service';
import { SLUG_REGEX } from '../validators';
import { Ecogesture } from '../ecogesture.model';
import { EcogestureService } from '../ecogesture.service';

interface FormValue {
  slug: string;
  biomId: string;
  isRounded: boolean;
  categoryIds: Array<number>;
  ecogestureIds: Array<number>;
  rank: number;
}

@Component({
  selector: 'biom-edit-indicator',
  templateUrl: './edit-indicator.component.html',
  styleUrls: ['./edit-indicator.component.scss']
})
export class EditIndicatorComponent implements OnInit {
  mode: 'create' | 'update' = 'create';
  editedIndicator: Indicator;
  valuedIndicator: ValuedIndicator;
  categories: Array<IndicatorCategory>;
  ecogestures: Array<Ecogesture>;
  isRounded: boolean;
  rank: number;
  isFetchingValues = false;
  fetchingError = false;
  spinnerIcon = faSpinner;
  addIcon = faPlusCircle;
  removeIcon = faMinusCircle;
  categoryIds: FormArray;
  ecogestureIds: FormArray;
  form: FormGroup;

  constructor(
    private route: ActivatedRoute,
    fb: FormBuilder,
    private indicatorService: IndicatorService,
    private indicatorCategoryService: IndicatorCategoryService,
    private ecogestureService: EcogestureService,
    private router: Router,
    private toastService: ToastService
  ) {
    this.categoryIds = fb.array(
      [null],
      // we need at least one category
      array => (array.value.filter((id: number) => id).length === 0 ? { required: true } : null)
    );
    this.ecogestureIds = fb.array([null]);
    this.isRounded = false;
    this.form = fb.group({
      slug: ['', [Validators.required, Validators.pattern(SLUG_REGEX)]],
      biomId: ['', [Validators.required, Validators.pattern(SLUG_REGEX)]],
      isRounded: this.isRounded,
      categoryIds: this.categoryIds,
      ecogestureIds: this.ecogestureIds,
      rank: this.rank
    });
  }

  get indicatorSectionDisplayed() {
    return this.isFetchingValues || this.valuedIndicator || this.fetchingError;
  }

  ngOnInit(): void {
    this.indicatorCategoryService.list().subscribe(categories => (this.categories = categories));
    this.ecogestureService.list().subscribe(ecogestures => (this.ecogestures = ecogestures));
    const indicatorId = this.route.snapshot.paramMap.get('indicatorId');
    if (indicatorId) {
      this.mode = 'update';
      this.indicatorService.get(+indicatorId).subscribe(indicator => {
        this.editedIndicator = indicator;
        const formValue: Partial<FormValue> = {
          slug: indicator.slug,
          biomId: indicator.biomId,
          isRounded: indicator.isRounded,
          rank: indicator.rank
        };
        this.form.patchValue(formValue);
        this.categoryIds.clear();
        indicator.categories.forEach(category => this.categoryIds.push(new FormControl(category.id)));
        // if the indicator has no category or ecogesture  (in case of manually created data)
        // add an empty one to fill
        if (!indicator.categories.length) {
          this.categoryIds.push(new FormControl(null));
        }
        this.ecogestureIds.clear();
        indicator.ecogestures.forEach(ecogesture => this.ecogestureIds.push(new FormControl(ecogesture.id)));
        if (!indicator.ecogestures.length) {
          this.ecogestureIds.push(new FormControl(null));
        }
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

  unselectedEcogestures(currentEcogesture: number) {
    // keep the current ecogesture and the other unselected ones
    return this.ecogestures
      ? this.ecogestures.filter(eco => eco.id === currentEcogesture || !(this.form.value as FormValue).ecogestureIds.includes(eco.id))
      : [];
  }

  addEcogesture() {
    this.ecogestureIds.push(new FormControl(null));
  }

  removeEcogesture(index: number) {
    this.ecogestureIds.removeAt(index);
  }

  save() {
    if (this.form.invalid) {
      return;
    }

    const formValue: FormValue = this.form.value;
    const command: IndicatorCommand = {
      slug: formValue.slug,
      biomId: formValue.biomId,
      categoryIds: formValue.categoryIds.filter(id => id),
      ecogestureIds: formValue.ecogestureIds.filter(id => id),
      isRounded: formValue.isRounded,
      rank: formValue.rank
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
    this.fetchingError = false;
    this.valuedIndicator = null;
    const biomId = (this.form.value as FormValue).biomId;
    this.indicatorService
      .getValues(biomId)
      .pipe(finalize(() => (this.isFetchingValues = false)))
      .subscribe({
        next: indicator => (this.valuedIndicator = indicator),
        error: () => (this.fetchingError = true)
      });
  }

  closeIndicatorSection() {
    this.valuedIndicator = null;
    this.fetchingError = null;
  }
}
