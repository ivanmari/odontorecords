import { Component,EventEmitter	 } from '@angular/core';
import { OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { PatientSelect } from './patient-select.component'


@Component({
  selector: 'app-root',
  
  template: `
  <mat-toolbar color="primary">
    <span>OdontoRecords</span>
    <span class="health-indicator" [class.up]="isBackendUp" [class.down]="!isBackendUp" title="Backend Status"></span>
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
    .health-indicator {
      width: 12px;
      height: 12px;
      border-radius: 50%;
      margin-left: 10px;
      display: inline-block;
      border: 2px solid white;
    }
    .health-indicator.up {
      background-color: #4caf50;
    }
    .health-indicator.down {
      background-color: #f44336;
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
  isBackendUp: boolean = false;

  constructor(private http: HttpClient) {}

	ngOnInit(): void {
    this.checkHealth();
    setInterval(() => this.checkHealth(), 30000); // Check every 30 seconds
	}

  checkHealth() {
    this.http.get('/health').subscribe({
      next: (data: any) => {
        this.isBackendUp = data.status === 'UP';
      },
      error: () => {
        this.isBackendUp = false;
      }
    });
  }

  onSearch(event: Event) {
    const target = event.target as HTMLInputElement;
    this.searchTerm = target.value;
  }
}
