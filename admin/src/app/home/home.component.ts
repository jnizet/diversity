import { Component, OnInit } from '@angular/core';
import { PageService } from '../page.service';
import { Observable } from 'rxjs';
import { PageLinks } from '../page.model';
import { ModalService } from '../modal.service';
import { CreateMediaPageModalComponent } from '../create-media-page-modal/create-media-page-modal.component';
import { first } from 'rxjs/operators';

@Component({
  selector: 'biom-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  pageLinks$: Observable<PageLinks>;

  constructor(private pageService: PageService, private modalService: ModalService) {}

  ngOnInit(): void {
    this.pageLinks$ = this.pageService.getPageLinks();
  }

  openMediaModal(target: string) {
    const modalRef = this.modalService.open(CreateMediaPageModalComponent, { size: 'xl', scrollable: true });
    modalRef.componentInstance.title = `Créer une nouvelle page ${target}`;
    modalRef.componentInstance.message = `Identifiant de la page ${target}`;

    modalRef.result.pipe(first()).subscribe(result => location.assign(`/admin/page-models/${target}/pages/create?name=${result}`));
  }
}
