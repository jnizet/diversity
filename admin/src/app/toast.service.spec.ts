import { TestBed } from '@angular/core/testing';

import { Toast, ToastService } from './toast.service';

describe('ToastService', () => {
  let service: ToastService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ToastService);
  });

  it('should signal errors and successes', () => {
    const toasts: Array<Toast> = [];
    service.toasts().subscribe(toast => toasts.push(toast));

    service.error("Une erreur inattendue (400) s'est produite sur le serveur\u00a0: foo");
    service.success('Oui');

    expect(toasts).toEqual([
      {
        message: `Une erreur inattendue (400) s'est produite sur le serveur\u00a0: foo`,
        type: 'error'
      },
      {
        message: 'Oui',
        type: 'success'
      }
    ]);
  });
});
