import { Injectable, TemplateRef, Type } from '@angular/core';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EMPTY, from, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

/**
 * The options that are passed when opening a modal.
 * If `errorOnClose` is true, then canceling the modal makes the returned observable emit an error.
 * Otherwise, the observable just doesn't emit anything and completes.
 */
export interface ModalOptions extends NgbModalOptions {
  errorOnClose?: boolean;
}

export class Modal<T> {
  constructor(private ngbModalRef: NgbModalRef, private errorOnClose: boolean) {}

  get componentInstance(): T {
    return this.ngbModalRef.componentInstance;
  }

  get result() {
    return from(this.ngbModalRef.result).pipe(catchError(err => (this.errorOnClose ? throwError(err || 'not confirmed') : EMPTY)));
  }
}

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  constructor(private ngbModal: NgbModal) {}

  /**
   * Opens a modal containing an instance of the given component, and returns a `Modal` instance,
   * The `Modal` instance contains the `componentInstance`,
   * and a `result` field, which is an observable, which emits and completes if the user validates.
   * You can also give options to the modal.
   * If `errorOnClose` is true, then canceling the modal makes the returned observable emit an error.
   * Otherwise, the observable just doesn't emit anything and completes.
   */
  open<T>(modalComponent: Type<T> | TemplateRef<any>, options?: ModalOptions): Modal<T> {
    return new Modal(this.ngbModal.open(modalComponent, options), options?.errorOnClose ?? false);
  }
}
