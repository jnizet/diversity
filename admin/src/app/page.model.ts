import { MediaCategory } from './media-category.model';

export type ElementType = 'TEXT' | 'LINK' | 'IMAGE' | 'LIST' | 'LIST_UNIT' | 'SECTION' | 'SELECT' | 'CHECKBOX' | 'MULTI_LIST';
export type SourceType = 'IMPORTED' | undefined;
interface BasePageElement {
  type: ElementType;
  id: number;
  name: string;
  description: string;
  source?: SourceType;
}

export interface LinkElement extends BasePageElement {
  type: 'LINK';
  text: string;
  href: string;
}

export interface TextElement extends BasePageElement {
  type: 'TEXT';
  multiLine: boolean;
  text: string;
  optional: boolean;
}

export interface SelectElement extends BasePageElement {
  type: 'SELECT';
  value: string;
  options: { [id: string]: string };
}

export interface CheckboxElement extends BasePageElement {
  type: 'CHECKBOX';
  value: boolean;
}

export interface ImageElement extends BasePageElement {
  type: 'IMAGE';
  multiSize: boolean;
  document: boolean;
  imageId: number;
  alt: string;
}

export interface ContainerElement extends BasePageElement {
  elements: Array<PageElement>;
}

export interface ListUnitElement extends ContainerElement {
  type: 'LIST_UNIT';
}

export interface ListElement extends ContainerElement {
  type: 'LIST';
  elements: Array<ListUnitElement>;
}

export interface SectionElement extends ContainerElement {
  type: 'SECTION';
}

export interface MultiListElement extends ContainerElement {
  type: 'MULTI_LIST';
  elements: Array<SectionElement>;
  templates: Array<SectionElement>;
}

export type PageElement =
  | TextElement
  | LinkElement
  | ImageElement
  | ListElement
  | ListUnitElement
  | SectionElement
  | SelectElement
  | MultiListElement
  | CheckboxElement;

function isValidText(text: TextElement): boolean {
  return !text.optional ? !!text.text : true;
}

function isValidSelect(select: SelectElement): boolean {
  return !!select.options;
}

function isValidCheckbox(checkbox: CheckboxElement): boolean {
  return typeof checkbox.value === 'boolean';
}

function isValidLink(link: LinkElement): boolean {
  return !!link.text && !!link.href;
}

function isValidImage(image: ImageElement): boolean {
  return !!image.imageId && !!image.alt;
}

function isValidCollection(collection: ListElement | ListUnitElement | SectionElement | MultiListElement): boolean {
  return collection.elements.length > 0 && !collection.elements.some(element => !isValidElement(element));
}

export function isValidElement(element: PageElement | null): boolean {
  if (!element) {
    return false;
  }
  switch (element.type) {
    case 'LIST':
    case 'LIST_UNIT':
    case 'MULTI_LIST':
    case 'SECTION':
      return isValidCollection(element);
    case 'TEXT':
      return isValidText(element);
    case 'LINK':
      return isValidLink(element);
    case 'IMAGE':
      return isValidImage(element);
    case 'SELECT':
      return isValidSelect(element);
    case 'CHECKBOX':
      return isValidCheckbox(element);
  }
}

export interface Page {
  id: number;
  name: string;
  modelName: string;
  title: string;
  description: string;
  elements: Array<PageElement>;
}

export interface ElementCommand {
  key: string;
  type: 'TEXT' | 'LINK' | 'IMAGE' | 'SELECT' | 'CHECKBOX';
}

export interface LinkCommand extends ElementCommand {
  text: string;
  href: string;
}

export interface TextCommand extends ElementCommand {
  text: string;
}

export interface SelectCommand extends ElementCommand {
  value: string;
}

export interface CheckboxCommand extends ElementCommand {
  value: boolean;
}

export interface ImageCommand extends ElementCommand {
  imageId: number;
  alt: string;
}

export interface PageCommand {
  title: string;
  name: string;
  elements: Array<ElementCommand>;
}

function textElementToCommand(key: string, element: TextElement): Array<ElementCommand> {
  const command: TextCommand = { key, text: element.text, type: 'TEXT' };
  return [command];
}

function selectElementToCommand(key: string, element: SelectElement): Array<ElementCommand> {
  const command: SelectCommand = { key, value: element.value, type: 'SELECT' };
  return [command];
}

function checkboxElementToCommand(key: string, element: CheckboxElement): Array<ElementCommand> {
  const command: CheckboxCommand = { key, value: element.value, type: 'CHECKBOX' };
  return [command];
}

function linkElementToCommand(key: string, element: LinkElement): Array<ElementCommand> {
  const command: LinkCommand = { key, text: element.text, href: element.href, type: 'LINK' };
  return [command];
}

function imageElementToCommand(key: string, element: ImageElement): Array<ElementCommand> {
  const command: ImageCommand = { key, imageId: element.imageId, alt: element.alt, type: 'IMAGE' };
  return [command];
}

function sectionElementToCommand(key: string, element: SectionElement): Array<ElementCommand> {
  const commands: Array<ElementCommand> = [];
  element.elements.forEach(sectionElement => commands.push(...elementToCommand(`${key}.`, sectionElement)));
  return commands;
}

function listElementToCommand(key: string, element: ListElement | MultiListElement): Array<ElementCommand> {
  // a list has "list units" as elements.
  // for each list unit we build a command, with a key composed of the list name and the current index.
  // for example:
  // {
  //   name: "slides",
  //   elements: [
  //     { elements: [ { name: "image", ... }, { name: "link", ...} ],
  //     { elements: [ { name: "image", ... }, { name: "link", ...} ],
  //   ]
  // }
  // returns:
  // [
  //   { type: "IMAGE", key: "slides.0.image" },
  //   { type: "LINK", key: "slides.0.link" },
  //   { type: "IMAGE", key: "slides.1.image" },
  //   { type: "LINK", key: "slides.1.link" },
  // ]
  const commands: Array<ElementCommand> = [];
  element.elements.forEach((listUnitElement, index) => {
    listUnitElement.elements.forEach(unitElement => {
      commands.push(...elementToCommand(`${key}.${index}.`, unitElement));
    });
  });
  return commands;
}

function multilistElementToCommand(key: string, element: ListElement | MultiListElement): Array<ElementCommand> {
  // a list has "list units" as elements.
  // for each list unit we build a command, with a key composed of the list name and the current index.
  // for example:
  // {
  //   name: "slides",
  //   elements: [
  //     { elements: [ { name: "image", ... }, { name: "link", ...} ],
  //     { elements: [ { name: "image", ... }, { name: "link", ...} ],
  //   ]
  // }
  // returns:
  // [
  //   { type: "IMAGE", key: "slides.0.image" },
  //   { type: "LINK", key: "slides.0.link" },
  //   { type: "IMAGE", key: "slides.1.image" },
  //   { type: "LINK", key: "slides.1.link" },
  // ]
  const commands: Array<ElementCommand> = [];
  element.elements.forEach((listUnitElement, index) => {
    const unitName = listUnitElement.name;
    listUnitElement.elements.forEach(unitElement => {
      commands.push(...elementToCommand(`${key}.${index}.${unitName}.`, unitElement));
    });
  });
  return commands;
}

/**
 * Builds an array of commands for the given page element at the specified key.
 * Leaf elements return an array with a single element, whereas lists and sections returns an array with several ones.
 */
export function elementToCommand(prefix: string, element: PageElement): Array<ElementCommand> {
  const key = `${prefix}${element.name}`;
  switch (element.type) {
    case 'IMAGE': {
      return imageElementToCommand(key, element);
    }
    case 'TEXT': {
      return textElementToCommand(key, element);
    }
    case 'LINK': {
      return linkElementToCommand(key, element);
    }
    case 'MULTI_LIST': {
      return multilistElementToCommand(key, element);
    }
    case 'LIST': {
      return listElementToCommand(key, element);
    }
    case 'SECTION': {
      return sectionElementToCommand(key, element);
    }
    case 'SELECT': {
      return selectElementToCommand(key, element);
    }
    case 'CHECKBOX': {
      return checkboxElementToCommand(key, element);
    }
    case 'LIST_UNIT': {
      return []; // should never happen as the list unit is consumed by the list
    }
  }
}

export interface PageLink {
  id: number | null;
  name: string;
  modelName: string;
  title: string | null;
}

export interface MediaPageLink extends PageLink {
  categories: MediaCategory[];
}

export interface PageLinks {
  staticPageLinks: Array<PageLink>;
  indicatorPageLinks: Array<PageLink>;
  territoryPageLinks: Array<PageLink>;
  zonePageLinks: Array<PageLink>;
  ecogesturePageLinks: Array<PageLink>;
  interviewPageLinks: Array<PageLink>;
  articlePageLinks: Array<PageLink>;
  photoReportsPageLinks: Array<PageLink>;
}
