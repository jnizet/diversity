import { Controller } from 'stimulus';

export class QuestionItemController extends Controller {
  static targets = ['item', 'question', 'answer'];
  itemTarget: HTMLElement;
  questionTarget: HTMLElement;
  answerTarget: HTMLElement;

  questionClicked(event: Event) {
    event.preventDefault();
    this.toggleAnswer();
  }

  private toggleAnswer(): void {
    const isClosed = this.itemTarget.classList.contains('closed');
    if (isClosed) {
      this.itemTarget.classList.remove('closed');
      this.answerTarget.style.height = this.answerTarget.scrollHeight + 20 + 'px';
    } else {
      this.questionTarget.classList.add('closed');
      this.answerTarget.style.height = '0px';
    }
  }
}
