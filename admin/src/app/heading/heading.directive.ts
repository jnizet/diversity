import { Directive, ElementRef, Input, OnChanges, Renderer2 } from '@angular/core';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: 'biom-heading'
})
export class HeadingDirective implements OnChanges {
  @Input()
  level: number;

  @Input()
  title: string;

  constructor(private elementRef: ElementRef<HTMLElement>, private renderer: Renderer2) {}

  ngOnChanges(): void {
    const validLevel = this.level < 1 || this.level > 6 ? 6 : this.level;
    this.elementRef.nativeElement.innerHTML = '';
    const element = this.renderer.createElement(`h${validLevel}`);
    this.renderer.appendChild(element, this.renderer.createText(this.title));
    this.renderer.appendChild(this.elementRef.nativeElement, element);
  }
}
