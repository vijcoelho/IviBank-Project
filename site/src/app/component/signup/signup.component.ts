import { Component } from '@angular/core';
import { TopbarComponent } from "../topbar/topbar.component";
import { FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [TopbarComponent, FormsModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  account = {
    name: '',
    email: '',
    password: '',
    cpf: '',
    tokenType: 'favorite_color',
    tokenValue: '',
  };

  constructor(
    private authService: AuthService, 
    private snackBar: MatSnackBar, 
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.route.queryParams.subscribe(params => {
      if (params['email']) {
        this.account.email = params['email'];
      }
    });
  }

  onSubmit(): void {

    if (!this.account.name || !this.account.email || !this.account.password || !this.account.cpf || !this.account.tokenValue) {
      this.snackBar.open('Please fill out all the required fields', 'Close', {
        duration: 3000,
        panelClass: ['error-snackbar'],
      });
      return;
    }

    const dataToSend = {
      name: this.account.name,
      email: this.account.email,
      password: this.account.password,
      cpf: this.account.cpf,
      [this.account.tokenType]: this.account.tokenValue,
    };

    this.authService.signup(dataToSend).subscribe({
      next: (response) => {
        console.log('Account created successfully: ', response);
        this.snackBar.open('Account created successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar'],
        });
        this.router.navigate(['/main']);
      },
      error: (error) => {
        console.log('Account not created: ', error);

        if (error.status === 400 && error.error.message && (error.error.message.includes('email') || error.error.message.includes('cpf'))) {
          this.snackBar.open('Email or CPF already registered', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar'],
          });
        } else {
          this.snackBar.open('Error creating your account', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar'],
          });
        }
      },
    });
  }
}
