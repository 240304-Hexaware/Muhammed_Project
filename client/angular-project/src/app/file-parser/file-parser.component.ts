import { CommonModule } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-file-parser',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-parser.component.html',
  styleUrl: './file-parser.component.css'
})
export class FileParserComponent {
  url : string = "http://localhost:8080/";
  title = 'client';
  httpClient: HttpClient;
  file: File | undefined;
  downloadFile: Blob | undefined;
  public responseData: any;

  constructor(httpClient: HttpClient) {
    /* the HttpClient needs to be provided, see app.config.ts */
    this.httpClient = httpClient;
  }

  fileSelected(event: any) {
    this.file = event.target.files[0];
    console.log("File selected: ", event.target.files[0])
  }
  
  upload(){}

  parseFile() {
    if(this.file == undefined) {
      alert("Please select a file");
      return;
    }

    let form : FormData = new FormData();
    form.append("file", this.file); // body
    let response = this.httpClient.post(this.url + "parseFile", form, {
      observe: "response",
      responseType: "json"
    });

    response.subscribe({
      next: (data) => {
        console.log("data: ", data);
        this.responseData = data.body;
        // let fileName: string | null = "file.txt";
        // let fileBody: string | null = data.body;
        // this.downloadFile = new Blob([fileBody as string], {type: "text/plain"});
      },
      error: (error: HttpErrorResponse) => {
        console.log("error: ", error);
        alert(error.message);
      }
    });

  }

  getObjectKeys(obj : any) : string[]{
    return Object.keys(obj);
  }
}
