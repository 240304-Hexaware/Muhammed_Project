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

  login() {  
    this.loginService.login(this.username, this.password).subscribe({
      next: (response) => {
          console.log(response.body); // Handle plain text response
          if(response.body == "Login Successful. Token set in cookie.") {
            this.loginService.isLoggedIn = true;
            this.loginService.setUser(this.username);
            console.log("Username: " + this.loginService.getUser());
            localStorage.setItem('username', this.username);
            this.router.navigate(['/parseFile']);
          }
          else {
            alert("Invalid username or password");
            this.loginService.isLoggedIn = false;
          }
          console.log("logged in: " + this.loginService.isLoggedIn);

      },
      error: (error) => {
        console.error(error); // Handle error
      }
    });
  }

  
}
