import { NgModule } from '@angular/core';
import { NgbConfig } from '@ng-bootstrap/ng-bootstrap';

@NgModule()
export class NgbTestingModule {
  constructor(ngbConfig: NgbConfig) {
    ngbConfig.animation = false;
  }
}
