import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FileParserComponent } from './file-parser/file-parser.component';
import { RecordsComponent } from './records/records.component';
import { Router } from '@angular/router';
import { UploadComponent } from './upload/upload.component';
import { NavComponent } from './nav/nav.component';
import { CommonModule } from '@angular/common';
import { LoginService } from './login/login.service';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FileParserComponent, RecordsComponent, UploadComponent, NavComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'angular-project';

  constructor(private router: Router) {}

}
