import { Component } from '@angular/core';
import { UploadService } from './upload.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [CommonModule, FormsModule, NgFor],
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css'
})
export class UploadComponent {
  file: File | undefined;
  public responseData: any;
  fileNames : Array<string> = [];

  constructor(private uploadService : UploadService) {}

  ngOnInit(): void {
    this.getFileNames();
  }

  fileSelected(event: any) {
    this.file = event.target.files[0];
    console.log("File selected: ", event.target.files[0])
  }

  upload(){
    if(this.file == undefined) {
      alert("Please select a file");
      return;
    }

    this.uploadService.uploadFile(this.file).subscribe({
      next: (data) => {
        console.log("data: ", data);
        this.responseData = data.body;
        this.getFileNames();
      },
      error: (error: HttpErrorResponse) => {
        console.log("error: ", error);
        alert(error.message);
      }
    });
  }

  getFileNames() : void {
    this.uploadService.getFileNames().subscribe({
        next: (names) => {
          console.log(names);
          this.fileNames = names;
        }
      }
    )
  }
}
