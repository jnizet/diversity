import { Component, OnInit } from '@angular/core';
import { faCheckCircle, faExclamationCircle } from '@fortawesome/free-solid-svg-icons';

import { Toast, ToastService } from '../toast.service';

@Component({
  selector: 'biom-toasts',
  templateUrl: './toasts.component.html',
  styleUrls: ['./toasts.component.scss'],
  // tslint:disable-next-line:no-host-metadata-property
  host: { '[class.ngb-toasts]': 'true' }
})
export class ToastsComponent implements OnInit {
  toasts: Array<Toast> = [];

  constructor(private toastService: ToastService) {}

  ngOnInit() {
    this.toastService.toasts().subscribe(toast => this.toasts.push(toast));
  }

  remove(toast: Toast) {
    this.toasts.splice(this.toasts.indexOf(toast), 1);
  }

  icon(toast: Toast) {
    return toast.type === 'success' ? faCheckCircle : faExclamationCircle;
  }
}
