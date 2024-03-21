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
    // Navigate to route '/route1'
    this.router.navigate(['/parseFile']);
  }

  goToRecords() {
    // Navigate to route '/route2'
    this.router.navigate(['/records']);
  }
}
