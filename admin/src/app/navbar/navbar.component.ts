import { Component, OnInit } from '@angular/core';
import { faChartLine, faPowerOff, faTags } from '@fortawesome/free-solid-svg-icons';
import { CurrentUserService } from '../current-user.service';
import { AuthenticatedUser } from '../authentication.service';
import { Observable } from 'rxjs';
import { WindowService } from '../window.service';

@Component({
  selector: 'biom-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  collapsed = true;
  indicatorCategoriesIcon = faTags;
  indicatorsIcon = faChartLine;
  logoutIcon = faPowerOff;

  user$: Observable<AuthenticatedUser>;

  constructor(private currentUserService: CurrentUserService, private windowService: WindowService) {}

  ngOnInit() {
    this.user$ = this.currentUserService.get();
  }

  logout(event: Event) {
    event.preventDefault();
    this.currentUserService.set(null);
    this.windowService.setLocationHref('/');
  }
}
