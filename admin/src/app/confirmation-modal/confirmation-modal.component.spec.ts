import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfirmationOptions, ConfirmationService } from '../confirmation.service';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbTestingModule } from '../ngb-testing.module';

class ModalComponentTester {
  constructor(private fixture: ComponentFixture<any>) {}

  detectChanges() {
    this.fixture.detectChanges();
  }

  get modalWindow(): HTMLElement {
    return document.querySelector('ngb-modal-window');
  }

  get modalBackdrop(): HTMLElement {
    return document.querySelector('ngb-modal-backdrop');
  }

  get modalBody(): HTMLElement {
    return document.querySelector('.modal-body');
  }

  get modalTitle(): HTMLElement {
    return document.querySelector('.modal-title');
  }

  yes() {
    (document.querySelector('#yes-button') as HTMLButtonElement).click();
    this.detectChanges();
  }

  no() {
    (document.querySelector('#no-button') as HTMLButtonElement).click();
    this.detectChanges();
  }
}

/**
 * A test component just to be able to create a fixture to detect changes
 */
@Component({
  template: ''
})
class TestComponent {}

describe('ConfirmationModalComponent and ConfirmationService', () => {
  let tester: ModalComponentTester;
  let confirmationService: ConfirmationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [NgbModalModule, NgbTestingModule],
      declarations: [TestComponent]
    });

    confirmationService = TestBed.inject(ConfirmationService);
    tester = new ModalComponentTester(TestBed.createComponent(TestComponent));
    tester.detectChanges();
  });

  afterEach(() => {
    if (tester.modalWindow) {
      tester.modalWindow.parentElement.removeChild(tester.modalWindow);
    }
    if (tester.modalBackdrop) {
      tester.modalBackdrop.parentElement.removeChild(tester.modalBackdrop);
    }
  });

  function confirm(options: ConfirmationOptions): Observable<void> {
    const result = confirmationService.confirm(options);
    tester.detectChanges();
    return result;
  }

  it('should display a modal dialog when confirming and use default title', () => {
    confirm({ message: 'Voulez-vous vraiment supprimer cette catégorie\u00a0?' });
    expect(tester.modalWindow).toBeTruthy();
    expect(tester.modalTitle.textContent).toBe('Confirmation');
    expect(tester.modalBody.textContent).toContain('Voulez-vous vraiment supprimer cette catégorie\u00a0?');
  });

  it('should honor the title option', () => {
    confirm({ message: 'Voulez-vous vraiment supprimer cette catégorie\u00a0?', title: 'Vous êtes sûr(e)?' });
    expect(tester.modalTitle.textContent).toBe('Vous êtes sûr(e)?');
  });

  it('should emit when confirming', (done: DoneFn) => {
    confirm({ message: 'Voulez-vous vraiment supprimer cette catégorie\u00a0?' }).subscribe(() => done());
    tester.yes();

    expect(tester.modalWindow).toBeFalsy();
  });

  it('should error when not confirming and errorOnClose is true', (done: DoneFn) => {
    confirm({ message: 'Voulez-vous vraiment supprimer cette catégorie\u00a0?', errorOnClose: true }).subscribe({ error: () => done() });
    tester.no();

    expect(tester.modalWindow).toBeFalsy();
  });

  it('should do nothing when not confirming and errorOnClose is not set', (done: DoneFn) => {
    confirm({ message: 'Voulez-vous vraiment supprimer cette catégorie\u00a0?' }).subscribe({
      error: () => fail(),
      complete: () => done()
    });
    tester.no();

    expect(tester.modalWindow).toBeFalsy();
  });
});
