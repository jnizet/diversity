export function showElement(element: Element) {
  element.classList.remove('d-none');
}

export function hideElement(element: Element) {
  element.classList.add('d-none');
}

export function setElementVisible(element: Element, visible: boolean) {
  if (visible) {
    showElement(element);
  } else {
    hideElement(element);
  }
}
