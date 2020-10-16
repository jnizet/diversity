import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { PageLink } from '../../page.model';
import { faFile, faFileMedical } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'biom-page-link',
  templateUrl: './page-link.component.html',
  styleUrls: ['./page-link.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PageLinkComponent {
  @Input()
  pageLink: PageLink;

  noPageIcon = faFileMedical;
  pageIcon = faFile;
}
