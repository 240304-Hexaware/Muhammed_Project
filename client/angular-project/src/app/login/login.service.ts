import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  isLoggedIn : boolean = false;
  url : string = 'http://localhost:8080';
  username : string = "";
  constructor(private httpClient : HttpClient, private router : Router) { }

  login(username: string, password: string) {
    return this.httpClient.post(this.url + "/login", { username, password }, {
      observe: "response",
      responseType: "text"
    });
  }

  setUser(username: string) {
    this.username = username;
  }

  getUser() {
    return this.username;
  }

  logout() {
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }
}
