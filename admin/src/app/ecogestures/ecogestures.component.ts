import { Component, OnInit } from '@angular/core';
import { Ecogesture } from '../ecogesture.model';
import { faHandHoldingHeart, faPlus, faTrash } from '@fortawesome/free-solid-svg-icons';
import { EcogestureService } from '../ecogesture.service';
import { ConfirmationService } from '../confirmation.service';
import { ToastService } from '../toast.service';
import { switchMap, tap } from 'rxjs/operators';

@Component({
  selector: 'biom-ecogestures',
  templateUrl: './ecogestures.component.html',
  styleUrls: ['./ecogestures.component.scss']
})
export class EcogesturesComponent implements OnInit {
  ecogestures: Array<Ecogesture>;

  ecogestureIcon = faHandHoldingHeart;
  createEcogestureIcon = faPlus;
  deleteEcogestureIcon = faTrash;

  constructor(
    private ecogestureService: EcogestureService,
    private confirmationService: ConfirmationService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.ecogestureService.list().subscribe(ecogestures => (this.ecogestures = ecogestures));
  }

  deleteEcogesture(ecogesture: Ecogesture) {
    this.confirmationService
      .confirm({ message: 'Voulez-vous vraiment supprimer cet éco-geste et sa page\u00a0?' })
      .pipe(
        switchMap(() => this.ecogestureService.delete(ecogesture.id)),
        tap(() => this.toastService.success(`L'éco-geste ${ecogesture.slug} a été supprimé`)),
        switchMap(() => this.ecogestureService.list())
      )
      .subscribe(ecogestures => (this.ecogestures = ecogestures));
  }
}
