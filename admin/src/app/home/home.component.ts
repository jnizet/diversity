import { Component, OnInit } from '@angular/core';
import { PageService } from '../page.service';
import { first, Observable, switchMap } from 'rxjs';
import { MediaPageLink, PageLink, PageLinks } from '../page.model';
import { ModalService } from '../modal.service';
import { CreateMediaPageModalComponent, MediaPageResult } from '../create-media-page-modal/create-media-page-modal.component';
import { faFile } from '@fortawesome/free-solid-svg-icons';
import { MediaService } from '../media.service';
import { Router } from '@angular/router';

type MediaModelName = 'article' | 'interview' | 'report';

function mediaModelLabel(name: MediaModelName) {
  switch (name) {
    case 'article':
      return 'Article';
    case 'interview':
      return 'Entretien';
    case 'report':
      return 'Reportage photo';
  }
}

@Component({
  selector: 'biom-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  pageLinks$: Observable<PageLinks>;
  pageIcon = faFile;

  constructor(
    private pageService: PageService,
    private modalService: ModalService,
    private mediaService: MediaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.pageLinks$ = this.pageService.getPageLinks();
  }

  openCreateMediaModal(modelName: MediaModelName) {
    const modalRef = this.modalService.open(CreateMediaPageModalComponent, { size: 'xl', scrollable: true });

    const label = mediaModelLabel(modelName);
    modalRef.componentInstance.title = `Créer une nouvelle page ${label}`;
    modalRef.componentInstance.message = `Identifiant de la page ${label}`;

    modalRef.result.pipe(first()).subscribe((result: MediaPageResult) =>
      this.router.navigate(['page-models', modelName, 'pages', 'create'], {
        queryParams: {
          name: result.name,
          categories: `${result.values.join(',')}`
        }
      })
    );
  }

  openEditMediaModal(modelName: MediaModelName, pageLink: PageLink) {
    const modalRef = this.modalService.open(CreateMediaPageModalComponent, { size: 'xl', scrollable: true });

    const label = mediaModelLabel(modelName);
    modalRef.componentInstance.title = `Modifier la page ${pageLink.name}`;
    modalRef.componentInstance.message = `Identifiant de la page ${label}`;
    modalRef.componentInstance.initalName = pageLink.name;
    modalRef.componentInstance.isReadonly = true;
    modalRef.componentInstance.intialCategories = (pageLink as MediaPageLink).categories;

    modalRef.result
      .pipe(switchMap(result => this.mediaService.update(pageLink.id, { id: pageLink.id, categoriesId: result.values })))
      .subscribe(() => this.router.navigate(['page-models', modelName, 'pages', pageLink.id, 'edit']));
  }
}
