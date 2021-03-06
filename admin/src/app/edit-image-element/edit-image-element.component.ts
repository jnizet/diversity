import { Component, forwardRef, Input, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { ImageElement } from '../page.model';
import { ModalService } from '../modal.service';
import { ImageService } from '../image.service';
import { finalize } from 'rxjs';
import { faFilePdf, faImage } from '@fortawesome/free-solid-svg-icons';
import { IdGeneratorService } from '../id-generator.service';

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
  documentIcon = faFilePdf;

  private onChange: (value: ImageElement) => void = () => {};
  private onTouched: () => void = () => {};

  idSuffix: string;

  constructor(
    private fb: FormBuilder,
    private modalService: ModalService,
    private imageService: ImageService,
    idGenerator: IdGeneratorService
  ) {
    this.idSuffix = idGenerator.generateSuffix();
    this.imageGroup = this.fb.group({
      imageId: [null, Validators.required],
      alt: ['', Validators.required]
    });
    this.imageGroup.valueChanges.subscribe((value: { imageId: number; alt: string }) => {
      if (this.imageGroup.valid) {
        this.onChange({ ...this.editedImageElement, ...value });
      } else {
        this.onChange(null);
      }
    });
    this.imageGroup.statusChanges.subscribe(() => this.onTouched());
  }

  @Input()
  set submitted(isSubmitted: boolean) {
    if (isSubmitted) {
      this.imageGroup.markAllAsTouched();
    }
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  updateElement(element: ImageElement) {
    this.imageGroup.setValue(
      {
        imageId: element.imageId,
        alt: element.alt
      },
      { emitEvent: false }
    );
    this.uploading = false;
  }

  writeValue(element: ImageElement): void {
    this.editedImageElement = element;
    if (element.source === 'IMPORTED') {
      this.uploading = true;
      this.imageService.addImportedImage(element.id);
      this.imageService
        .importImage(element.imageId, element.multiSize, element.document)
        .pipe(finalize(() => (this.uploading = false)))
        .subscribe(image => {
          this.imageService.setImageLoadingToFalse(this.editedImageElement.id);
          return this.imageGroup.patchValue({ imageId: image.id, alt: element.alt });
        });
    } else {
      this.updateElement(element);
    }
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
    if (this.editedImageElement.multiSize) {
      return '.jpg,.jpeg';
    } else if (this.editedImageElement.document) {
      return '.pdf';
    } else {
      return '.jpg,.jpeg,.png,.gif,.svg';
    }
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

  onUpload(event: Event) {
    this.uploading = true;

    const fileInput = event.target as HTMLInputElement;
    const file = fileInput.files[0];
    this.imageService
      .createImage(file, this.editedImageElement.multiSize, this.editedImageElement.document)
      .pipe(finalize(() => (this.uploading = false)))
      .subscribe(image => {
        return this.imageGroup.patchValue({ imageId: image.id });
      });
    fileInput.value = '';
  }
}
