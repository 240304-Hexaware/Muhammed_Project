import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { UploadComponent } from '../upload/upload.component';
import { RecordsComponent } from '../records/records.component';
import { FileParserComponent } from '../file-parser/file-parser.component';
import { CommonModule } from '@angular/common';
import { LoginService } from '../login/login.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterOutlet, RecordsComponent, UploadComponent],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {

  title = 'angular-project';
  username : string | null = localStorage.getItem('username');
  constructor(private router: Router, public loginService : LoginService) {}

  logout() {
    this.loginService.logout();
  }
}
