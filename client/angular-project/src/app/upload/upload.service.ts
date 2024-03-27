import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  url : string = "http://localhost:8080";
  constructor(private httpClient : HttpClient) { }
  
  uploadFile(file : File) {
    let formData : FormData = new FormData();
    formData.append("file", file); // body
    return this.httpClient.post(this.url + "/uploadFile", formData, {
      observe: "response",
      responseType: "json"
    });
  }

  getFileNames() : Observable<string[]> {
    return this.httpClient.get<string[]>(this.url + "/viewFileNames");
  }
}
