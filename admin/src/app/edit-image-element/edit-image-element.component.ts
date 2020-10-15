import { Component, forwardRef, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { ImageElement } from '../page.model';
import { ModalService } from '../modal.service';
import { ImageService } from '../image.service';
import { finalize } from 'rxjs/operators';
import { faImage } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'biom-edit-image-element',
  templateUrl: './edit-image-element.component.html',
  styleUrls: ['./edit-image-element.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => EditImageElementComponent),
      multi: true
    }
  ]
})
export class EditImageElementComponent {
  editedImageElement: ImageElement;
  imageGroup: FormGroup;
  uploading = false;
  imageIcon = faImage;

  private onChange: (value: ImageElement) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private fb: FormBuilder, private modalService: ModalService, private imageService: ImageService) {
    this.imageGroup = this.fb.group({
      imageId: [null, Validators.required],
      alt: ['', Validators.required]
    });
    this.imageGroup.valueChanges.subscribe((value: { imageId: number; alt: string }) =>
      this.onChange({ ...this.editedImageElement, ...value })
    );
    this.imageGroup.statusChanges.subscribe(() => this.onTouched());
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  writeValue(element: ImageElement): void {
    this.editedImageElement = element;
    this.imageGroup.setValue(
      {
        imageId: element.imageId,
        alt: element.alt
      },
      { emitEvent: false }
    );
  }

  get imageUrl() {
    const imageId = this.imageGroup.value.imageId;
    const suffix = this.editedImageElement.multiSize ? '/sm' : '';
    return `/images/${imageId}/image${suffix}`;
  }

  get largeImageUrl() {
    const imageId = this.imageGroup.value.imageId;
    return `/images/${imageId}/image`;
  }

  get sourceSet() {
    if (!this.editedImageElement.multiSize) {
      return null;
    }
    const imageId = this.imageGroup.value.imageId;
    return `/images/${imageId}/image/sm 576w, /images/${imageId}/image/md 768w, /images/${imageId}/image/lg 1200w, /images/${imageId}/image/xl 1900w`;
  }

  get acceptedFiles(): string {
    return this.editedImageElement.multiSize ? '.jpg,.jpeg' : '.jpg,.jpeg,.png,.gif,.svg';
  }

  openImageModal(content: TemplateRef<any>) {
    this.modalService.open(content, { size: 'xl', scrollable: true });
  }

  chooseImage(input: HTMLInputElement) {
    input.click();
  }

  markImageAsTouched() {
    this.imageGroup.get('imageId').markAsTouched();
  }

  upload(event: Event) {
    this.uploading = true;

    const fileInput = event.target as HTMLInputElement;
    const file = fileInput.files[0];

    this.imageService
      .createImage(file, this.editedImageElement.multiSize)
      .pipe(finalize(() => (this.uploading = false)))
      .subscribe(image => this.imageGroup.patchValue({ imageId: image.id }));

    fileInput.value = '';
  }
}