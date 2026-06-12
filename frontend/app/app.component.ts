import { Component,EventEmitter	 } from '@angular/core';
import { OnInit } from '@angular/core';

import { PatientSelect } from './patient-select.component'


@Component({
  selector: 'my-app',
  template: `
  <mat-toolbar color="primary">
    <span>OdontoRecords</span>
    <span style="flex: 1 1 auto;"></span>
    <mat-form-field appearance="outline" style="font-size: 14px;">
      <mat-label>Search Patient</mat-label>
      <input matInput (keyup)="onSearch($event)" placeholder="Name or Last Name">
    </mat-form-field>
  </mat-toolbar>

  <div style="padding: 20px;">
    <patient-select [searchQuery]="searchQuery">Loading... </patient-select>
  </div>
  `
})
export class AppComponent implements OnInit {
  searchQuery: string = '';

  ngOnInit(): void {
  }

  onSearch(event: any): void {
    this.searchQuery = event.target.value;
  }
}
