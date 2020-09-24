import { Component, OnInit } from '@angular/core';
import { IndicatorCategory, IndicatorCategoryCommand } from '../indicator-category.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IndicatorCategoryService } from '../indicator-category.service';
import { ToastService } from '../toast.service';
import { Observable } from 'rxjs';

interface FormValue {
  name: string;
}

@Component({
  selector: 'biom-edit-indicator-category',
  templateUrl: './edit-indicator-category.component.html',
  styleUrls: ['./edit-indicator-category.component.scss']
})
export class EditIndicatorCategoryComponent implements OnInit {
  mode: 'create' | 'update' = 'create';
  editedIndicatorCategory: IndicatorCategory;
  form: FormGroup;

  constructor(
    private route: ActivatedRoute,
    fb: FormBuilder,
    private indicatorCategoryService: IndicatorCategoryService,
    private router: Router,
    private toastService: ToastService
  ) {
    this.form = fb.group({
      name: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const indicatorCategoryId = this.route.snapshot.paramMap.get('indicatorCategoryId');
    if (indicatorCategoryId) {
      this.mode = 'update';
      this.indicatorCategoryService.get(+indicatorCategoryId).subscribe(indicatorCategory => {
        this.editedIndicatorCategory = indicatorCategory;

        const formValue: FormValue = {
          name: indicatorCategory.name
        };
        this.form.setValue(formValue);
      });
    }
  }

  save() {
    if (this.form.invalid) {
      return;
    }

    const formValue: FormValue = this.form.value;
    const command: IndicatorCategoryCommand = formValue;

    let obs: Observable<IndicatorCategory | void>;
    if (this.mode === 'update') {
      obs = this.indicatorCategoryService.update(this.editedIndicatorCategory.id, command);
    } else {
      obs = this.indicatorCategoryService.create(command);
    }

    obs.subscribe(() => {
      this.router.navigate(['/indicator-categories']);
      this.toastService.success(`La catégorie d'indicateur a été ${this.mode === 'update' ? 'modifiée' : 'créée'}`);
    });
  }
}
