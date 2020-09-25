import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../authentication.service';
import { Router } from '@angular/router';
import { CurrentUserService } from '../current-user.service';
import { animate, classBasedAnimation } from '../animation';

@Component({
  selector: 'biom-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.scss']
})
export class AuthenticationComponent {
  form: FormGroup;

  @ViewChild('authenticationButton')
  authenticationButton: ElementRef<HTMLButtonElement>;

  constructor(
    fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private currentUserService: CurrentUserService,
    private router: Router
  ) {
    this.form = fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  login() {
    if (this.form.invalid) {
      return;
    }

    this.authenticationService.login(this.form.value).subscribe({
      next: user => {
        this.currentUserService.set(user);
        this.router.navigate(['/']);
      },
      error: () => animate(this.authenticationButton.nativeElement, classBasedAnimation('shake')).subscribe()
    });
  }
}
