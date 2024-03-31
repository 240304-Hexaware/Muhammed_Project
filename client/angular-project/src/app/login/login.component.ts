import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LoginService } from './login.service';
import { FormsModule } from '@angular/forms';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterOutlet],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  username : string = "";
  password : string = "";
  constructor(private loginService: LoginService, private router : Router) {};

  login() { // call auth service to 
    this.loginService.login(this.username, this.password).subscribe({
      next: (response) => {
          console.log(response.body); // Handle plain text response
          if(response.body == "Login Successful. Token set in cookie.") {
            this.loginService.isLoggedIn = true;
            this.router.navigate(['/parseFile']);
            console.log("logged in: " + this.loginService.isLoggedIn);
          }
          else {
            console.log("logged in: " + this.loginService.isLoggedIn);
            this.loginService.isLoggedIn = false;
          }
      },
      error: (error) => {
        console.error(error); // Handle error
      }
    });
  }

  
}
