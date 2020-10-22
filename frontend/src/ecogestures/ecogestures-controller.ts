import { Controller } from 'stimulus';
import { setElementVisible } from '../elements';

const MINIMUM_THRESHOLD = 6;

export class EcogesturesController extends Controller {
  static targets = ['ecogesture', 'more'];

  ecogestureTargets: Array<HTMLElement>;
  moreTarget: HTMLButtonElement;
  ecogesturesDisplayed = 0;

  connect() {
    // set the first 6 elements visible, and hide the following ones
    this.ecogestureTargets.forEach((ecogesture, index) => {
      const isVisible = index < MINIMUM_THRESHOLD;
      setElementVisible(ecogesture, isVisible);
    });
    // store how many are really displayed
    this.ecogesturesDisplayed = Math.min(this.ecogestureTargets.length, MINIMUM_THRESHOLD);
    // update the button visibility (might not be necessary to display it)
    this.updateSeeMoreButtonVisibility();
  }

  seeMore() {
    // set the 6 following elements visible, and hide the rest
    this.ecogestureTargets.forEach((ecogesture, index) => {
      const isVisible = index < this.ecogesturesDisplayed + MINIMUM_THRESHOLD;
      setElementVisible(ecogesture, isVisible);
    });
    // store how many are really displayed
    this.ecogesturesDisplayed = Math.min(this.ecogestureTargets.length, this.ecogesturesDisplayed + MINIMUM_THRESHOLD);
    // update the button visibility (might not be necessary to display it anymore)
    this.updateSeeMoreButtonVisibility();
  }

  private updateSeeMoreButtonVisibility() {
    // visible if there are more than the 6, but only if they are not all displayed
    const isButtonVisible = this.ecogestureTargets.length > MINIMUM_THRESHOLD && this.ecogesturesDisplayed < this.ecogestureTargets.length;
    setElementVisible(this.moreTarget, isButtonVisible);
  }
}
