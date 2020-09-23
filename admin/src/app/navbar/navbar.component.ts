import { Component } from '@angular/core';
import { faTags } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'biom-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  collapsed = true;
  indicatorCategoriesIcon = faTags;
}
