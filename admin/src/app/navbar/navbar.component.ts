import { Component } from '@angular/core';
import { faChartLine, faTags } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'biom-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  collapsed = true;
  indicatorCategoriesIcon = faTags;
  indicatorsIcon = faChartLine;
}
