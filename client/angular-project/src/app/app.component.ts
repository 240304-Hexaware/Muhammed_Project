import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FileParserComponent } from './file-parser/file-parser.component';
import { RecordsComponent } from './records/records.component';
import { Router } from '@angular/router';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FileParserComponent, RecordsComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'angular-project';

  constructor(private router: Router) {}

  goToFileParser() {
    this.router.navigate(['/parseFile']);
  }

  goToRecords() {
    this.router.navigate(['/records']);
  }
  goToUpload() {
    this.router.navigate(['/upload']);
  }
}
