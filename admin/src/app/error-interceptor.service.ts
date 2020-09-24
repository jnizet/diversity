import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ToastService } from './toast.service';
import { Injectable } from '@angular/core';

@Injectable()
export class ErrorInterceptorService implements HttpInterceptor {
  constructor(private toastService: ToastService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap({
        error: error => this.handleError(error)
      })
    );
  }

  private handleError(error: HttpErrorResponse) {
    const status = error.status;
    if (status) {
      // The backend returned an unsuccessful response code.
      const body = error.error;
      const functionalError = body?.functionalError;

      if (status === 400 && functionalError) {
        this.toastService.error(functionalError);
      } else {
        const errorMessage = body?.message ?? body;
        this.toastService.error(`Une erreur inattendue (${status}) s'est produite sur le serveur\u00a0: ${errorMessage}`);
      }
    } else {
      // A client-side or network error occurred.
      this.toastService.error(`Une erreur inattendue s'est produite à l'envoi d'une requête au serveur`);
    }
  }
}
