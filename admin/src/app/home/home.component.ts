import { Component, OnInit } from '@angular/core';
import { PageService } from '../page.service';
import { Observable } from 'rxjs';
import { PageLinks } from '../page.model';

@Component({
  selector: 'biom-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  pageLinks$: Observable<PageLinks>;

  constructor(private pageService: PageService) {}

  ngOnInit(): void {
    this.pageLinks$ = this.pageService.getPageLinks();
  }
}
