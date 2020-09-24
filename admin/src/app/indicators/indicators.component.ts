import { Component, OnInit } from '@angular/core';
import { faChartLine, faPlus, faTrash } from '@fortawesome/free-solid-svg-icons';
import { switchMap, tap } from 'rxjs/operators';

import { ConfirmationService } from '../confirmation.service';
import { ToastService } from '../toast.service';
import { Indicator } from '../indicator.model';
import { IndicatorService } from '../indicator.service';

@Component({
  selector: 'biom-indicators',
  templateUrl: './indicators.component.html',
  styleUrls: ['./indicators.component.scss']
})
export class IndicatorsComponent implements OnInit {
  indicators: Array<Indicator>;

  indicatorIcon = faChartLine;
  createIndicatorIcon = faPlus;
  deleteIndicatorIcon = faTrash;

  constructor(
    private indicatorService: IndicatorService,
    private confirmationService: ConfirmationService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.indicatorService.list().subscribe(indicators => (this.indicators = indicators));
  }

  deleteIndicator(indicator: Indicator) {
    this.confirmationService
      .confirm({ message: 'Voulez-vous vraiment supprimer cet indicateur\u00a0?' })
      .pipe(
        switchMap(() => this.indicatorService.delete(indicator.id)),
        tap(() => this.toastService.success(`L'indicateur ${indicator.slug} a été supprimée`)),
        switchMap(() => this.indicatorService.list())
      )
      .subscribe(indicators => (this.indicators = indicators));
  }

  categories(indicator: Indicator) {
    return indicator.categories
      .map(category => category.name)
      .sort()
      .join(', ');
  }
}
