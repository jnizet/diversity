import { Component, OnInit } from '@angular/core';
import { Ecogesture, EcogestureCommand } from '../ecogesture.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EcogestureService } from '../ecogesture.service';
import { ToastService } from '../toast.service';
import { Observable } from 'rxjs';
import { SLUG_REGEX } from '../validators';

interface FormValue {
  slug: string;
}

@Component({
  selector: 'biom-edit-ecogesture',
  templateUrl: './edit-ecogesture.component.html',
  styleUrls: ['./edit-ecogesture.component.scss']
})
export class EditEcogestureComponent implements OnInit {
  mode: 'create' | 'update' = 'create';
  editedEcogesture: Ecogesture;
  form: FormGroup;

  constructor(
    private route: ActivatedRoute,
    fb: FormBuilder,
    private ecogestureService: EcogestureService,
    private router: Router,
    private toastService: ToastService
  ) {
    this.form = fb.group({
      slug: ['', [Validators.required, Validators.pattern(SLUG_REGEX)]]
    });
  }

  ngOnInit(): void {
    const ecogestureId = this.route.snapshot.paramMap.get('ecogestureId');
    if (ecogestureId) {
      this.mode = 'update';
      this.ecogestureService.get(+ecogestureId).subscribe(ecogesture => {
        this.editedEcogesture = ecogesture;

        const formValue: FormValue = {
          slug: ecogesture.slug
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
    const command: EcogestureCommand = formValue;

    let obs: Observable<Ecogesture | void>;
    if (this.mode === 'update') {
      obs = this.ecogestureService.update(this.editedEcogesture.id, command);
    } else {
      obs = this.ecogestureService.create(command);
    }

    obs.subscribe(() => {
      this.router.navigate(['/ecogestures']);
      this.toastService.success(`L'éco-geste a été ${this.mode === 'update' ? 'modifié' : 'créé'}`);
    });
  }
}
