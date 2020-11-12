// A slug has the form words-with-sometimes-numbers-like-2
// So the regex is one or more repetition of [a-z0-9]
// followed by zero or more (not captured) groups of hyphen plus [a-z0-9] repetition
import { AbstractControl, FormArray } from '@angular/forms';
import { isValidElement, PageElement } from './page.model';

export const SLUG_REGEX = /^[a-z0-9]+(?:-[a-z0-9]+)*$/;

export function atLeastOneElement(control: AbstractControl) {
  const elements = (control as FormArray).value as Array<PageElement>;
  return elements.length > 0 ? null : { atLeastOne: true };
}

export function validElement(control: AbstractControl) {
  const element = control.value as PageElement;
  return isValidElement(element) ? null : { invalidElement: true };
}

export function validList(control: AbstractControl) {
  const elements = control.value as Array<PageElement>;
  if (!elements) {
    return { invalidElement: true };
  }
  return elements.some(element => !isValidElement(element)) ? { invalidElement: true } : null;
}
