import { CommonModule, NgFor } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-records',
  standalone: true,
  imports: [CommonModule, FormsModule, NgFor],
  templateUrl: './records.component.html',
  styleUrl: './records.component.css',
})
export class RecordsComponent {
  selectedRecordType: string = "car";
  
  recordTypes: string[] = ["car", "jet", "boat"];

  carFields : string[] = ["manufacturer", "model", "year"];
  jetFields : string[] = ["manufacturer", "model", "year", "engineType"];
  boatFields : string[] = ["manufacturer", "model", "year", "color", "length"];
  recordTypeFields : { [key: string]: string[]} = {
    "car": this.carFields,
    "jet": this.jetFields,
    "boat": this.boatFields
  }
  responseData : any;
  url : string = "http://localhost:8080/";
  httpClient : HttpClient;

  constructor(httpClient : HttpClient) {
    this.httpClient = httpClient;
  };

  filterCriteria : Map<string, string> = new Map<string, string>();

  selectType(event: Event) {
    const selectedValue = (event.target as HTMLSelectElement).value; // type cast to get value
    this.selectedRecordType = selectedValue;
  }

  getFieldsForSelectedRecordType() {
    return this.recordTypeFields[this.selectedRecordType] || [];
  }

  updateFilterCriteria(field : string, event : Event) {
    const fieldValue = (event.target as HTMLSelectElement).value; // type cast to get value
    this.filterCriteria.set(field, fieldValue);
    
  }

  filter() {
    if(this.filterCriteria.size < 1) {
      alert("Please Enter Filter Criteria");
      return;
    }
    
    let params = new HttpParams();
    this.filterCriteria.forEach((value, key) => {
      console.log(key + ": " + value);
      params = params.append(key, value);
    });

    console.log(params);      
    
    this.httpClient.get<string[]>(this.url + "parsedRecords/filter", {
      params:params,
      observe: "response",
      responseType: "json"
    }).subscribe({
      next: (data) => {
        console.log(data);
        if(data.body != null) {
          this.responseData = data.body.map(item => JSON.parse(item)); // Parse each item into an object
        }
        console.log("responseData:" + JSON.stringify(this.responseData));
      },
      error: (err) => {
        console.log("error: ", err);
        
      }
    })
  }

  getFields(obj : any) : string[] {
    return Object.keys(obj);
  }
}
