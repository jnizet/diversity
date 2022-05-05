import { Injectable } from '@angular/core';

/**
 * Service used to generate unique (sequential) ID suffixes. This is useful when the same form components
 * are used several times in the same page, to avoid having conflicts between the IDs of their form elements.
 * In addition to being invalid HTML, having two checkboxes with the same ID in the page for example
 * will cause the first one to be checked/unchecked when the label of the second one is clicked, which is of course not expected.
 * This is a service and not just a function to be able to mock it and have reliable IDs in tests
 */
@Injectable({
  providedIn: 'root'
})
export class IdGeneratorService {
  private nextId = 0;

  constructor() {}

  generateSuffix(): string {
    return `-${this.nextId++}`;
  }
}
