import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { UploadComponent } from '../upload/upload.component';
import { RecordsComponent } from '../records/records.component';
import { FileParserComponent } from '../file-parser/file-parser.component';
import { CommonModule } from '@angular/common';
import { LoginService } from '../login/login.service';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RecordsComponent, UploadComponent],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {

  title = 'angular-project';

  constructor(private router: Router, public loginService : LoginService) {}

  // goToFileParser() {
  //   this.router.navigate(['/parseFile']);
  // }
  // goToRecords() {
  //   this.router.navigate(['/records']);
  // }
  // goToUpload() {
  //   this.router.navigate(['/upload']);
  // }
  logout() {
    this.loginService.logout();
  }
}
