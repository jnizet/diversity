import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'biom-creation-modal',
  templateUrl: './create-media-page-modal.component.html',
  styleUrls: ['./create-media-page-modal.component.scss']
})
export class CreateMediaPageModalComponent {
  @Input() message: string;
  @Input() title: string;

  constructor(public activeModal: NgbActiveModal) {}
}
