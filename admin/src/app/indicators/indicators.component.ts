import { Component, OnInit } from '@angular/core';
import { faAngleDown, faAngleUp, faChartLine, faPlus, faTrash } from '@fortawesome/free-solid-svg-icons';
import { switchMap, tap } from 'rxjs';

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
  moveUpItemIcon = faAngleUp;
  moveDownItemIcon = faAngleDown;
  constructor(
    private indicatorService: IndicatorService,
    private confirmationService: ConfirmationService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.indicatorService.list().subscribe(indicators => (this.indicators = indicators.sort((a, b) => a.rank - b.rank)));
  }

  deleteIndicator(indicator: Indicator) {
    this.confirmationService
      .confirm({ message: 'Voulez-vous vraiment supprimer cet indicateur et sa page\u00a0?' })
      .pipe(
        switchMap(() => this.indicatorService.delete(indicator.id)),
        tap(() => this.toastService.success(`L'indicateur ${indicator.slug} a été supprimée`)),
        switchMap(() => this.indicatorService.list())
      )
      .subscribe(indicators => (this.indicators = indicators));
  }

  moveUp(indicatorIndex: number) {
    this.indicatorService.swap(this.indicators[indicatorIndex].id, this.indicators[indicatorIndex - 1].id).subscribe(indicators => {
      this.indicators = indicators.sort((a, b) => a.rank - b.rank);
    });
  }

  moveDown(indicatorIndex: number) {
    this.indicatorService.swap(this.indicators[indicatorIndex].id, this.indicators[indicatorIndex + 1].id).subscribe(indicators => {
      this.indicators = indicators.sort((a, b) => a.rank - b.rank);
    });
  }

  categories(indicator: Indicator) {
    return indicator.categories
      .map(category => category.name)
      .sort()
      .join(', ');
  }
}
