import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  username : string = "";
  password : string = "";
  role : string = "";
  url : string = 'http://localhost:8080';

  constructor(private httpClient : HttpClient, private router : Router) {};

  register() {
    let body = {
      "username": this.username,
      "password": this.password,
      "role": this.role
    }
    this.httpClient.post(this.url + "/registration", body, {
      observe: "response",
      responseType: "text"
    }).subscribe({
      next: (response) => {
        console.log(response.body);
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.log(err);
      }

    })
  }
}
