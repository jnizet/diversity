import { TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import { ComponentTester } from 'ngx-speculoos';
import { PageLinkComponent } from './page-link/page-link.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { RouterTestingModule } from '@angular/router/testing';
import { PageService } from '../page.service';
import { of } from 'rxjs';

class HomeComponentTester extends ComponentTester<HomeComponent> {
  constructor() {
    super(HomeComponent);
  }

  get staticPageLinks() {
    return this.elements('ul')[0].elements('biom-page-link');
  }

  get indicatorLinks() {
    return this.elements('ul')[1].elements('biom-page-link');
  }

  get territoryLinks() {
    return this.elements('ul')[2].elements('biom-page-link');
  }

  get zoneLinks() {
    return this.elements('ul')[3].elements('biom-page-link');
  }

  get ecogestureLinks() {
    return this.elements('ul')[4].elements('biom-page-link');
  }
  get articlePageLinks() {
    return this.elements('ul')[5].elements('biom-page-link');
  }
  get interviewPageLinks() {
    return this.elements('ul')[6].elements('biom-page-link');
  }
}

describe('HomeComponent', () => {
  let tester: HomeComponentTester;

  beforeEach(() => {
    const pageService = jasmine.createSpyObj<PageService>('PageService', ['getPageLinks']);
    pageService.getPageLinks.and.returnValue(
      of({
        staticPageLinks: [
          {
            id: 1,
            name: 'Home',
            modelName: 'home',
            title: 'Accueil'
          }
        ],
        indicatorPageLinks: [
          {
            id: 2,
            name: 'i1',
            modelName: 'indicator',
            title: 'Indicateur 1'
          }
        ],
        territoryPageLinks: [
          {
            id: 3,
            name: 'guadeloupe',
            modelName: 'territory',
            title: 'Guadeloupe'
          }
        ],
        zonePageLinks: [
          {
            id: 41,
            name: 'antilles',
            modelName: 'territory-zone',
            title: 'Bassin Antillais'
          }
        ],
        ecogesturePageLinks: [
          {
            id: 4,
            name: 'e1',
            modelName: 'ecogesture',
            title: 'Ecogeste 1'
          }
        ],
        articlePageLinks: [
          {
            id: 12,
            name: 'e1',
            modelName: 'article',
            title: 'Article 1'
          }
        ],
        interviewPageLinks: [
          {
            id: 15,
            name: 'e1',
            modelName: 'interview',
            title: 'Interview 1'
          }
        ]
      })
    );

    TestBed.configureTestingModule({
      imports: [FontAwesomeModule, RouterTestingModule],
      declarations: [HomeComponent, PageLinkComponent],
      providers: [{ provide: PageService, useValue: pageService }]
    });
    tester = new HomeComponentTester();
    tester.detectChanges();
  });

  it('should all page links', () => {
    expect(tester.staticPageLinks.length).toBe(1);
    expect(tester.staticPageLinks[0]).toContainText('Accueil');

    expect(tester.indicatorLinks.length).toBe(1);
    expect(tester.indicatorLinks[0]).toContainText('Indicateur 1');

    expect(tester.territoryLinks.length).toBe(1);
    expect(tester.territoryLinks[0]).toContainText('Guadeloupe');

    expect(tester.zoneLinks.length).toBe(1);
    expect(tester.zoneLinks[0]).toContainText('Bassin Antillais');

    expect(tester.ecogestureLinks.length).toBe(1);
    expect(tester.ecogestureLinks[0]).toContainText('Ecogeste 1');

    expect(tester.articlePageLinks.length).toBe(1);
    expect(tester.articlePageLinks[0]).toContainText('Article 1');

    expect(tester.interviewPageLinks.length).toBe(1);
    expect(tester.interviewPageLinks[0]).toContainText('Interview 1');
  });
});
