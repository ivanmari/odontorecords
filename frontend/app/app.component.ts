import { Component,EventEmitter	 } from '@angular/core';
import { OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { PatientSelect } from './patient-select.component'


@Component({
  selector: 'app-root',
  
  template: `
  <mat-toolbar color="primary">
    <button mat-icon-button (click)="sidenav.toggle()">
      <mat-icon>menu</mat-icon>
    </button>
    <span>OdontoRecords</span>
    <span class="health-indicator" [class.up]="isBackendUp" [class.down]="!isBackendUp" title="Backend Status"></span>
    <span class="spacer"></span>
  </mat-toolbar>
  
  <mat-sidenav-container class="sidenav-container">
    <mat-sidenav #sidenav mode="side" opened class="sidenav">
      <mat-nav-list>
        <a mat-list-item (click)="selectedMenu = 'patients'; patientViewMode = 'search'; selectedPatientId = null" [class.active]="selectedMenu === 'patients'">
          <mat-icon matListIcon>people</mat-icon>
          <span matLine>Patients</span>
        </a>
        <a mat-list-item (click)="selectedMenu = 'accounting'" [class.active]="selectedMenu === 'accounting'">
          <mat-icon matListIcon>account_balance</mat-icon>
          <span matLine>Accounting</span>
        </a>
        <a mat-list-item (click)="selectedMenu = 'inventory'" [class.active]="selectedMenu === 'inventory'">
          <mat-icon matListIcon>inventory_2</mat-icon>
          <span matLine>Inventory</span>
        </a>
      </mat-nav-list>
    </mat-sidenav>

    <mat-sidenav-content>
      <div [ngSwitch]="selectedMenu" class="content">
        <div *ngSwitchCase="'patients'">
          <div *ngIf="patientViewMode === 'search'">
            <div layout="row" layout-align="space-between center" class="search-container">
              <mat-form-field appearance="outline" class="search-field" flex="70">
                <mat-label>Search Patient</mat-label>
                <input matInput (keyup)="onSearch($event)" placeholder="Enter patient surname">
                <mat-icon matSuffix>search</mat-icon>
              </mat-form-field>
              <div flex="25" style="text-align: right;">
                <button mat-raised-button color="accent" (click)="onNewPatient()">
                  <mat-icon>person_add</mat-icon> New Patient
                </button>
              </div>
            </div>
            <patient-select [searchTerm]="searchTerm" (confirmed)="onPatientConfirmed($event)">Loading... </patient-select>
          </div>

          <div *ngIf="patientViewMode === 'details'">
            <patient-details [patientId]="selectedPatientId" (back)="patientViewMode = 'search'"></patient-details>
          </div>
        </div>

        <div *ngSwitchCase="'accounting'">
          <accounting-module></accounting-module>
        </div>

        <div *ngSwitchCase="'inventory'">
          <inventory-list></inventory-list>
        </div>
      </div>
    </mat-sidenav-content>
  </mat-sidenav-container>

  `,
  styles: [`
    .sidenav-container {
      height: calc(100vh - 64px);
    }
    .sidenav {
      width: 200px;
      background-color: #fafafa;
    }
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
    .search-container {
      margin-bottom: 20px;
    }
    .search-field {
      width: 100%;
      max-width: 400px;
    }
    .content {
      padding: 20px;
    }
    .active {
      background-color: rgba(0, 0, 0, 0.04);
      color: #3f51b5;
    }
    [matListIcon] {
      margin-right: 16px;
    }
  `]
})

export class AppComponent implements OnInit {
  searchTerm: string = '';
  isBackendUp: boolean = false;
  selectedMenu: string = 'patients';
  patientViewMode: 'search' | 'details' = 'search';
  selectedPatientId: string | null = null;

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

  onPatientConfirmed(patientId: string) {
    this.selectedPatientId = patientId;
    this.patientViewMode = 'details';
  }

  onNewPatient() {
    this.selectedPatientId = null;
    this.patientViewMode = 'details';
  }
}
