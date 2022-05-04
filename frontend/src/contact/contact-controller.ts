import { Controller } from 'stimulus';
import { hideElement, setElementVisible, showElement } from '../elements';

const MAX_CHARACTERS = 700;
// taken from Angular
const EMAIL_REGEXP =
  /^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

export class ContactController extends Controller {
  static targets = [
    'popin',
    'from',
    'invalidFrom',
    'subject',
    'invalidSubject',
    'body',
    'invalidBody',
    'remainingCharacters',
    'send',
    'sendFailed'
  ];

  popinTarget: HTMLElement;
  fromTarget: HTMLInputElement;
  invalidFromTarget: HTMLDivElement;
  subjectTarget: HTMLInputElement;
  invalidSubjectTarget: HTMLDivElement;
  bodyTarget: HTMLTextAreaElement;
  invalidBodyTarget: HTMLDivElement;
  remainingCharactersTarget: HTMLSpanElement;
  sendTarget: HTMLButtonElement;
  sendFailedTarget: HTMLDivElement;

  connect() {
    this.remainingCharactersTarget.innerText = `${MAX_CHARACTERS}`;
  }

  open(event: Event) {
    event.stopPropagation();
    event.preventDefault();
    this.setVisible(true);
  }

  close(event: Event) {
    event.stopPropagation();
    event.preventDefault();
    this.setVisible(false);
  }

  private setVisible(visible: boolean) {
    setElementVisible(this.popinTarget, visible);
    if (visible) {
      this.fromTarget.focus();
    }
  }

  bodyChanged() {
    this.remainingCharactersTarget.innerText = `${MAX_CHARACTERS - this.bodyTarget.value.length}`;
  }

  async send(event: Event) {
    event.preventDefault();
    hideElement(this.sendFailedTarget);
    const formElements = [this.fromTarget, this.subjectTarget, this.bodyTarget, this.sendTarget];
    if (this.validate()) {
      formElements.forEach(e => (e.disabled = true));
      try {
        const response = await fetch('/messages', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            from: this.fromTarget.value,
            subject: this.subjectTarget.value,
            body: this.bodyTarget.value
          })
        });
        if (!response.ok) {
          throw new Error('Request failed.');
        }
        this.fromTarget.value = '';
        this.subjectTarget.value = '';
        this.bodyTarget.value = '';
        this.setVisible(false);
      } catch (e) {
        showElement(this.sendFailedTarget);
      } finally {
        formElements.forEach(e => (e.disabled = false));
      }
    }
  }

  private validate(): boolean {
    const fromValid = EMAIL_REGEXP.test(this.fromTarget.value);
    const subjectValid = this.subjectTarget.value.trim().length > 0;
    const bodyValid = this.bodyTarget.value.trim().length > 0;

    setElementVisible(this.invalidFromTarget, !fromValid);
    setElementVisible(this.invalidSubjectTarget, !subjectValid);
    setElementVisible(this.invalidBodyTarget, !bodyValid);
    return fromValid && bodyValid;
  }
}
