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
  url : string = "http://localhost:8080";
  title = 'client';
  httpClient: HttpClient;
  flatFile: File | undefined;
  specFile: File | undefined;
  downloadFile: Blob | undefined;
  public responseData: any;
  selectedRecordType: string = "car";
  recordTypes: string[] = ["car", "jet", "boat"];

  constructor(httpClient: HttpClient) {
    /* the HttpClient needs to be provided, see app.config.ts */
    this.httpClient = httpClient;
  }

  selectType(event: Event) {
    const selectedValue = (event.target as HTMLSelectElement).value; // type cast to get value
    this.selectedRecordType = selectedValue;
  }

  flatFileSelected(event: any) {
    this.flatFile = event.target.files[0];
    console.log("File selected: ", event.target.files[0])
  }

  specFileSelected(event: any) {
    this.specFile = event.target.files[0];
    console.log("File selected: ", event.target.files[0])
  }

  parseFile() {
    if(this.flatFile == undefined) {
      alert("Please select a flat file");
      return;
    }

    if(this.specFile == undefined) {
      alert("Please select a specification file");
      return;
    }

    // body
    let form : FormData = new FormData();
    form.append("flatFile", this.flatFile); 
    form.append("specFile", this.specFile); 
    form.append("_recordType", this.selectedRecordType); 
    let response = this.httpClient.post(this.url + "/parseFile", form, {
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
