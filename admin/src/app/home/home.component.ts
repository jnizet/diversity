import { Component, OnInit } from '@angular/core';
import { PageService } from '../page.service';
import { first, Observable, switchMap } from 'rxjs';
import { MediaPageLink, PageLink, PageLinks } from '../page.model';
import { ModalService } from '../modal.service';
import { CreateMediaPageModalComponent } from '../create-media-page-modal/create-media-page-modal.component';
import { faFile } from '@fortawesome/free-solid-svg-icons';
import { MediaService } from '../media.service';

@Component({
  selector: 'biom-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  pageLinks$: Observable<PageLinks>;
  pageIcon = faFile;

  constructor(private pageService: PageService, private modalService: ModalService, private mediaService: MediaService) {}

  ngOnInit(): void {
    this.pageLinks$ = this.pageService.getPageLinks();
  }

  openCreateMediaModal(target: string) {
    const modalRef = this.modalService.open(CreateMediaPageModalComponent, { size: 'xl', scrollable: true });

    modalRef.componentInstance.title = `Créer une nouvelle page ${target}`;
    modalRef.componentInstance.message = `Identifiant de la page ${target}`;

    modalRef.result
      .pipe(first())
      .subscribe(result =>
        location.assign(`/admin/page-models/${target}/pages/create?name=${result.name}&categories=${result.values.join(',')}`)
      );
  }

  openEditMediaModal(target: string, pageLink: PageLink) {
    const modalRef = this.modalService.open(CreateMediaPageModalComponent, { size: 'xl', scrollable: true });

    modalRef.componentInstance.title = `Modifie la page ${pageLink.name}`;
    modalRef.componentInstance.message = `Identifiant de la page`;
    modalRef.componentInstance.initalName = pageLink.name;
    modalRef.componentInstance.isreadonly = true;
    modalRef.componentInstance.intialCategories = (pageLink as MediaPageLink).categories;

    modalRef.result
      .pipe(switchMap(result => this.mediaService.update(pageLink.id, { id: pageLink.id, categoriesId: result.values })))
      .subscribe(() => location.assign(`/admin/page-models/${target}/pages/${pageLink.id}/edit`));
  }
}
