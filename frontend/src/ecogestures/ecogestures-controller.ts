import { Controller } from 'stimulus';
import { setElementVisible } from '../elements';

const MINIMUM_THRESHOLD = 4;

export class EcogesturesController extends Controller {
  static targets = ['ecogesture', 'more'];

  ecogestureTargets: Array<HTMLElement>;
  moreTarget: HTMLButtonElement;

  connect() {
    this.ecogestureTargets.forEach((ecogesture, index) => {
      const isVisible = index < MINIMUM_THRESHOLD;
      setElementVisible(ecogesture, isVisible);
    });
    const moreButtonIsVisible = this.ecogestureTargets.length > MINIMUM_THRESHOLD;
    // update the button visibility (might not be necessary to display it)
    this.updateSeeMoreButtonVisibility(moreButtonIsVisible);
  }

  seeMore() {
    // show the rest of elements
    this.ecogestureTargets.forEach((ecogesture, index) => {
      setElementVisible(ecogesture, true);
    });
    this.updateSeeMoreButtonVisibility(false);
  }

  private updateSeeMoreButtonVisibility(visible: boolean) {
    // visible if there are more than the 4, but only if they are not all displayed
    setElementVisible(this.moreTarget, visible);
  }
}
