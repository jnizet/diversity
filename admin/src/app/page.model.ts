export type ElementType = 'TEXT' | 'LINK' | 'IMAGE' | 'LIST' | 'LIST_UNIT' | 'SECTION';

interface BasePageElement {
  type: ElementType;
  id: number;
  name: string;
  description: string;
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
}

export interface ImageElement extends BasePageElement {
  type: 'IMAGE';
  multiSize: boolean;
  imageId: number;
  alt: string;
}

export interface ListUnitElement extends BasePageElement {
  type: 'LIST_UNIT';
  elements: Array<PageElement>;
}

export interface ListElement extends BasePageElement {
  type: 'LIST';
  elements: Array<ListUnitElement>;
}

export interface SectionElement extends BasePageElement {
  type: 'SECTION';
  elements: Array<PageElement>;
}

export type PageElement = TextElement | LinkElement | ImageElement | ListElement | ListUnitElement | SectionElement;

export interface Page {
  id: number;
  name: string;
  title: string;
  description: string;
  elements: Array<PageElement>;
}

export interface ElementCommand {
  key: string;
  type: 'TEXT' | 'LINK' | 'IMAGE';
}

export interface LinkCommand extends ElementCommand {
  text: string;
  href: string;
}

export interface TextCommand extends ElementCommand {
  text: string;
}

export interface ImageCommand extends ElementCommand {
  imageId: number;
  alt: string;
}

export interface PageCommand {
  title: string;
  elements: Array<ElementCommand>;
}

function textElementToCommand(key: string, element: TextElement): Array<ElementCommand> {
  const command: TextCommand = { key, text: element.text, type: 'TEXT' };
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

function listElementToCommand(key: string, element: ListElement): Array<ElementCommand> {
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
    case 'LIST': {
      return listElementToCommand(key, element);
    }
    case 'SECTION': {
      return sectionElementToCommand(key, element);
    }
    case 'LIST_UNIT': {
      return []; // should never happen as the list unit is consumed by the list
    }
  }
}