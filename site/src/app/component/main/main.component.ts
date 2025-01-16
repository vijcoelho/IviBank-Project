import { Component } from '@angular/core';
import { TopbarComponent } from "../topbar/topbar.component";
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-main',
  imports: [TopbarComponent],
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {

  constructor(private route: Router, private snackBar: MatSnackBar) {}

  onCreateAccount(): void {
    const email = (document.getElementById('email') as HTMLInputElement).value;

    if (!email) {
      this.snackBar.open('Please provide your email', 'Close', {
        duration: 3000,
        panelClass: ['error-snackbar'],
      });
      return;
    }

    this.route.navigate(['/signup'], { queryParams: {email: email } });
  }
}
