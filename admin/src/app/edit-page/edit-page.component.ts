import { Component, OnInit } from '@angular/core';
import { PageService } from '../page.service';
import { ElementCommand, elementToCommand, Page, PageCommand, PageElement } from '../page.model';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastService } from '../toast.service';
import { forkJoin, Observable } from 'rxjs';

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

  constructor(private route: ActivatedRoute, fb: FormBuilder, private pageService: PageService, private toastService: ToastService) {
    this.elementsGroup = fb.group({});
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
          this.elementsGroup.addControl(element.name, new FormControl(element));
        });
        this.pageForm.patchValue({
          title: page.title
        } as FormValue);
        this.pageModel = model;
      });
    } else {
      // in creation mode, we only fetch the page model with empty values
      this.pageService.getModel(modelName).subscribe(model => {
        this.editedPage = JSON.parse(JSON.stringify(model));
        this.editedPage.elements.forEach(element => {
          this.elementsGroup.addControl(element.name, new FormControl(element));
        });
        this.pageModel = model;
      });
    }
  }

  savePage() {
    if (this.pageForm.invalid) {
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
      name: this.editedPage.name ? this.editedPage.name : 'test', // TODO should be the proper slug
      elements: elementCommands
    };
    let obs: Observable<Page | void>;
    if (this.mode === 'update') {
      obs = this.pageService.update(this.editedPage.id, command);
    } else {
      obs = this.pageService.create(this.pageModel.modelName, command);
    }

    obs.subscribe(() => {
      this.toastService.success(`La page a été ${this.mode === 'update' ? 'modifiée' : 'créée'}`);
    });
  }

  getElementModel(name: string): PageElement {
    return this.pageModel.elements.find(el => el.name === name);
  }
}
