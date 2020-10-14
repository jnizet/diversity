import { Component, OnInit } from '@angular/core';
import { PageService } from '../page.service';
import { ElementCommand, elementToCommand, Page, PageCommand, PageElement } from '../page.model';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastService } from '../toast.service';
import { Observable } from 'rxjs';

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

  constructor(private route: ActivatedRoute, fb: FormBuilder, private pageService: PageService, private toastService: ToastService) {
    this.elementsGroup = fb.group({});
    this.pageForm = fb.group({
      title: ['', Validators.required],
      elements: this.elementsGroup
    });
  }

  ngOnInit(): void {
    const pageId = +this.route.snapshot.paramMap.get('pageId');
    if (pageId) {
      this.mode = 'update';
      this.pageService.get(pageId).subscribe(page => {
        this.editedPage = page;
        page.elements.forEach(element => {
          this.elementsGroup.addControl(element.name, new FormControl(element));
        });
        this.pageForm.patchValue({
          title: page.title
        } as FormValue);
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
      elements: elementCommands
    };
    let obs: Observable<Page | void>;
    if (this.mode === 'update') {
      obs = this.pageService.update(this.editedPage.id, command);
    } else {
      // obs = this.pageService.create(command);
    }

    obs.subscribe(() => {
      this.toastService.success(`La page a été ${this.mode === 'update' ? 'modifiée' : 'créée'}`);
    });
  }
}
