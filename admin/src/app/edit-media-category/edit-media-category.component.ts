import { Component, OnInit } from '@angular/core';
import { MediaCategory, MediaCategoryCommand } from '../media-category.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MediaCategoryService } from '../media-category.service';
import { ToastService } from '../toast.service';
import { Observable } from 'rxjs';

interface FormValue {
  name: string;
}

@Component({
  selector: 'biom-edit-media-category',
  templateUrl: './edit-media-category.component.html',
  styleUrls: ['./edit-media-category.component.scss']
})
export class EditMediaCategoryComponent implements OnInit {
  mode: 'create' | 'update' = 'create';
  editedMediaCategory: MediaCategory;
  form: FormGroup;

  constructor(
    private route: ActivatedRoute,
    fb: FormBuilder,
    private mediaCategoryService: MediaCategoryService,
    private router: Router,
    private toastService: ToastService
  ) {
    this.form = fb.group({
      name: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const mediaCategoryId = this.route.snapshot.paramMap.get('mediaCategoryId');
    if (mediaCategoryId) {
      this.mode = 'update';
      this.mediaCategoryService.get(+mediaCategoryId).subscribe(mediaCategory => {
        this.editedMediaCategory = mediaCategory;

        const formValue: FormValue = {
          name: mediaCategory.name
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
    const command: MediaCategoryCommand = formValue;

    let obs: Observable<MediaCategory | void>;
    if (this.mode === 'update') {
      obs = this.mediaCategoryService.update(this.editedMediaCategory.id, command);
    } else {
      obs = this.mediaCategoryService.create(command);
    }

    obs.subscribe(() => {
      this.router.navigate(['/media-categories']);
      this.toastService.success(`La catégorie d'indicateur a été ${this.mode === 'update' ? 'modifiée' : 'créée'}`);
    });
  }
}
