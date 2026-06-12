import { Component,EventEmitter	 } from '@angular/core';
import { OnInit } from '@angular/core';

import { PatientSelect } from './patient-select.component'


@Component({
  selector: 'app-root',
  
  template: `
  <mat-toolbar color="primary">
    <span>OdontoRecords</span>
    <span class="spacer"></span>
    <mat-form-field appearance="outline" class="search-field">
      <mat-label>Search Patient</mat-label>
      <input matInput (keyup)="onSearch($event)" placeholder="Enter patient name">
    </mat-form-field>
  </mat-toolbar>
  
  <div class="content">
    <patient-select [searchTerm]="searchTerm">Loading... </patient-select>
  </div>

  `,
  styles: [`
    .spacer {
      flex: 1 1 auto;
    }
    .search-field {
      font-size: 14px;
      margin-top: 15px;
    }
    .content {
      padding: 20px;
    }
  `]
})

export class AppComponent implements OnInit {
  searchTerm: string = '';

	ngOnInit(): void {				
	}

  onSearch(event: Event) {
    const target = event.target as HTMLInputElement;
    this.searchTerm = target.value;
  }
}
