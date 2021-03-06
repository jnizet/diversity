import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { PageService } from '../page.service';
import { ElementCommand, elementToCommand, Page, PageCommand, PageElement } from '../page.model';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastService } from '../toast.service';
import { forkJoin, Observable } from 'rxjs';
import { validElement } from '../validators';
import { animate, classBasedAnimation } from '../animation';
import { ImageService } from '../image.service';

interface FormValue {
  title: string;
  elements: { [key: string]: PageElement };
}

@Component({
  selector: 'biom-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrls: ['./edit-page.component.scss']
})
export class EditPageComponent implements OnInit {
  mode: 'create' | 'update' = 'create';
  editedPage: Page;
  pageForm: FormGroup;
  elementsGroup: FormGroup;
  pageModel: Page;
  submitted = false;
  categories?: string;

  @ViewChild('saveButton')
  saveButton: ElementRef<HTMLButtonElement>;

  constructor(
    private route: ActivatedRoute,
    fb: FormBuilder,
    private pageService: PageService,
    private toastService: ToastService,
    private imageService: ImageService,
    private router: Router
  ) {
    this.elementsGroup = fb.group({}, { validators: Validators.required });
    this.pageForm = fb.group({
      title: ['', Validators.required],
      elements: this.elementsGroup
    });
  }

  ngOnInit(): void {
    const modelName = this.route.snapshot.paramMap.get('modelName');
    const pageId = +this.route.snapshot.paramMap.get('pageId');

    if (pageId) {
      this.mode = 'update';
      // in edit mode, we fetch page with the current values
      // and the page model (same entity, but with all values populated with empty object)
      // allowing to display all fields, even these without values
      // (for example it allows to add an empty list unit to an empty list)
      forkJoin([this.pageService.getValues(pageId), this.pageService.getModel(modelName)]).subscribe(([page, model]) => {
        this.editedPage = page;
        page.elements.forEach(element => {
          this.elementsGroup.addControl(element.name, new FormControl(element, [Validators.required, validElement]));
        });
        this.pageForm.patchValue({
          title: page.title
        } as FormValue);
        this.pageModel = model;
      });
    } else {
      this.categories = this.route.snapshot.queryParamMap.get('categories');
      const pageName = this.route.snapshot.queryParamMap.get('name');
      // in creation mode, we only fetch the page model with empty values
      this.pageService.getModel(modelName).subscribe(model => {
        this.editedPage = JSON.parse(JSON.stringify(model));
        this.editedPage.name = pageName;
        this.editedPage.elements.forEach(element => {
          this.elementsGroup.addControl(element.name, new FormControl(element, [Validators.required, validElement]));
        });
        this.pageModel = model;
      });
    }
  }

  import() {
    this.pageService.importPageValue(this.editedPage.name, this.editedPage.modelName).subscribe(page => {
      this.pageForm.patchValue({
        title: page.title
      } as FormValue);
      page.elements.forEach(element => {
        if (this.elementsGroup.controls[element.name]) {
          element.source = 'IMPORTED';
          this.elementsGroup.controls[element.name].patchValue(element);
        }
      });
    });
  }

  scrollTo(elementId: string) {
    const el = document.getElementById(`page-element--${elementId}`);
    el.scrollIntoView({ behavior: 'smooth', inline: 'nearest' });
  }

  savePage() {
    this.submitted = true;
    if (this.pageForm.invalid) {
      animate(this.saveButton.nativeElement, classBasedAnimation('shake')).subscribe();
      return;
    }

    const formValue = this.pageForm.value as FormValue;

    // build the element commands from the form value
    const elementCommands: Array<ElementCommand> = [];
    Object.keys(formValue.elements).map(elementKey => {
      const element = formValue.elements[elementKey];
      const commands = elementToCommand('', element);
      elementCommands.push(...commands);
    });

    const command: PageCommand = {
      title: formValue.title,
      name: this.editedPage.name,
      elements: elementCommands
    };

    let obs: Observable<Page | void>;
    if (this.mode === 'update') {
      obs = this.pageService.update(this.editedPage.id, command);
    } else {
      obs = this.pageService.create(this.pageModel.modelName, command, this.categories);
    }

    obs.subscribe({
      next: () => {
        this.toastService.success(`La page a ??t?? ${this.mode === 'update' ? 'modifi??e' : 'cr????e'}`);
        this.router.navigate(['/']);
      },
      error: () => animate(this.saveButton.nativeElement, classBasedAnimation('shake')).subscribe()
    });
  }

  getElementModel(name: string): PageElement {
    return this.pageModel.elements.find(el => el.name === name);
  }

  get imageImported() {
    return this.imageService.importedImages;
  }

  get imageImportedLabel() {
    return `${this.imageImported.filter(image => !image.isLoading).length}/${this.imageImported.length} images import?? depuis la pr??prod`;
  }
}
