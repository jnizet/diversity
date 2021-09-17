import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { faMinusCircle, faPlusCircle } from '@fortawesome/free-solid-svg-icons';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MediaCategory } from '../media-category.model';
import { MediaCategoryService } from '../media-category.service';

interface FormValue {
  categoryIds: Array<number>;
  name: string;
}

@Component({
  selector: 'biom-creation-modal',
  templateUrl: './create-media-page-modal.component.html',
  styleUrls: ['./create-media-page-modal.component.scss']
})
export class CreateMediaPageModalComponent implements OnInit {
  @Input() message: string;
  @Input() title: string;
  @Input() initalName: string;
  @Input() intialCategories: MediaCategory[];
  @Input() isreadonly: boolean;

  categoryIds: FormArray;
  categories: Array<MediaCategory>;
  addIcon = faPlusCircle;
  removeIcon = faMinusCircle;
  form: FormGroup;

  constructor(public activeModal: NgbActiveModal, private mediaCategoryService: MediaCategoryService, fb: FormBuilder) {
    this.categoryIds = fb.array(
      [null],
      // we need at least one category
      array => (array.value.filter((id: number) => id).length === 0 ? { required: true } : null)
    );
    this.form = fb.group({
      name: ['', [Validators.required]],
      categoryIds: this.categoryIds
    });
  }

  ngOnInit(): void {
    this.mediaCategoryService.list().subscribe(categories => (this.categories = categories));
    const formValue: Partial<FormValue> = {
      name: this.initalName
    };
    this.form.patchValue(formValue);
    this.categoryIds.clear();
    if (this.intialCategories && this.intialCategories.length !== 0) {
      this.intialCategories.forEach(category => this.categoryIds.push(new FormControl(category.id)));
    } else {
      this.categoryIds.push(new FormControl(null));
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

  closeModal() {
    const formValue = this.form.value as FormValue;
    if (!this.form.invalid) {
      this.activeModal.close({ name: formValue.name, values: this.categoryIds.value });
    }
  }
}
