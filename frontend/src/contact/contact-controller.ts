import { Controller } from 'stimulus';
import { hideElement, setElementVisible, showElement } from '../elements';

const MAX_CHARACTERS = 700;
// taken from Angular
const EMAIL_REGEXP =
  /^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;

export class ContactController extends Controller {
  static targets = ['form', 'from', 'invalidFrom', 'body', 'invalidBody', 'remainingCharacters', 'send', 'sendFailed'];

  formTarget: HTMLFormElement;
  fromTarget: HTMLInputElement;
  invalidFromTarget: HTMLDivElement;
  bodyTarget: HTMLTextAreaElement;
  invalidBodyTarget: HTMLDivElement;
  remainingCharactersTarget: HTMLSpanElement;
  sendTarget: HTMLButtonElement;
  sendFailedTarget: HTMLDivElement;

  private visible = false;

  connect() {
    this.remainingCharactersTarget.innerText = `${MAX_CHARACTERS}`;
  }

  toggle(event: Event) {
    event.stopPropagation();
    event.preventDefault();
    this.setVisible(!this.visible);
  }

  private setVisible(visible: boolean) {
    this.visible = visible;
    setElementVisible(this.formTarget, visible)
    if (this.visible) {
      this.fromTarget.focus();
    }
  }

  bodyChanged() {
    this.remainingCharactersTarget.innerText = `${MAX_CHARACTERS - this.bodyTarget.value.length}`;
  }

  async send(event: Event) {
    event.preventDefault();
    hideElement(this.sendFailedTarget);
    const formElements = [this.fromTarget, this.bodyTarget, this.sendTarget];
    if (this.validate()) {
      formElements.forEach(e => e.disabled = true);
      try {
        await fetch('/messages', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ from: this.fromTarget.value, body: this.bodyTarget.value })
        });
        this.fromTarget.value = '';
        this.bodyTarget.value = '';
        this.setVisible(false);
      } catch (e) {
        showElement(this.sendFailedTarget);
      } finally {
        formElements.forEach(e => e.disabled = false);
      }
    }
  }

  private validate(): boolean {
    const fromValid = EMAIL_REGEXP.test(this.fromTarget.value);
    const bodyValid = this.bodyTarget.value.trim().length > 0;

    setElementVisible(this.invalidFromTarget, !fromValid);
    setElementVisible(this.invalidBodyTarget, !bodyValid);
    return fromValid && bodyValid;
  }
}
