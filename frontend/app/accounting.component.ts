import { Component, OnInit } from '@angular/core';
import { PatientService } from './patient.service';
import { PatientBasicInfo } from './patientbasic';
import { HttpClient } from '@angular/common/http';
import { Charge } from './charge';
import { Installment } from './installment';
import { map } from 'rxjs/operators';

@Component({
  selector: 'accounting-module',
  template: `
    <div class="accounting-container">
      <div class="search-section">
        <mat-form-field appearance="outline" class="search-field">
          <mat-label>Search Patient for Accounting</mat-label>
          <input matInput (keyup)="onSearch($event)" placeholder="Enter patient surname">
        </mat-form-field>
      </div>

      <div layout="row" class="main-content">
        <div flex="30" class="patient-list">
          <mat-card>
            <mat-list role="list">
              <mat-list-item role="listitem" *ngFor="let patient of patients"
                            [class.selected]="patient.id === selectedPatientId"
                            (click)="selectPatient(patient)">
                <div matLine>{{patient.fullName}}</div>
                <div matLine class="balance" [class.negative]="patient.balance < 0">
                  Balance: {{patient.balance}}
                </div>
              </mat-list-item>
            </mat-list>
          </mat-card>
        </div>

        <div flex="70" class="patient-history" *ngIf="selectedPatientId">
          <mat-card>
            <mat-card-header>
              <mat-card-title>Accounting History: {{selectedPatientName}}</mat-card-title>
              <div class="actions">
                <button mat-raised-button color="primary" (click)="toggleAddCharge()">Add Charge</button>
                <button mat-raised-button color="accent" (click)="toggleAddInstallment()">Add Payment</button>
              </div>
            </mat-card-header>

            <mat-card-content>
              <!-- Add Charge Form -->
              <div *ngIf="showAddCharge" class="form-container">
                <h3>Add Charge</h3>
                <div layout="column">
                  <mat-form-field>
                    <mat-label>Amount</mat-label>
                    <input matInput type="number" [(ngModel)]="newCharge.charge">
                  </mat-form-field>
                  <mat-form-field>
                    <mat-label>Details</mat-label>
                    <input matInput [(ngModel)]="newCharge.details">
                  </mat-form-field>
                  <mat-form-field>
                    <mat-label>Date</mat-label>
                    <input matInput [matDatepicker]="chargePicker" [(ngModel)]="newCharge.deliveryDate">
                    <mat-datepicker-toggle matSuffix [for]="chargePicker"></mat-datepicker-toggle>
                    <mat-datepicker #chargePicker></mat-datepicker>
                  </mat-form-field>
                  <div class="form-actions">
                    <button mat-button (click)="showAddCharge = false">Cancel</button>
                    <button mat-raised-button color="primary" (click)="addCharge()">Save Charge</button>
                  </div>
                </div>
              </div>

              <!-- Add Installment Form -->
              <div *ngIf="showAddInstallment" class="form-container">
                <h3>Add Payment (Installment)</h3>
                <div layout="column">
                  <mat-form-field>
                    <mat-label>Amount</mat-label>
                    <input matInput type="number" [(ngModel)]="newInstallment.amount">
                  </mat-form-field>
                  <mat-form-field>
                    <mat-label>Date</mat-label>
                    <input matInput [matDatepicker]="instPicker" [(ngModel)]="newInstallment.paymentDay">
                    <mat-datepicker-toggle matSuffix [for]="instPicker"></mat-datepicker-toggle>
                    <mat-datepicker #instPicker></mat-datepicker>
                  </mat-form-field>
                  <div class="form-actions">
                    <button mat-button (click)="showAddInstallment = false">Cancel</button>
                    <button mat-raised-button color="primary" (click)="addInstallment()">Save Payment</button>
                  </div>
                </div>
              </div>

              <mat-tabs>
                <mat-tab label="Charges">
                  <table mat-table [dataSource]="charges" class="full-width">
                    <ng-container matColumnDef="date">
                      <th mat-header-cell *matHeaderCellDef> Date </th>
                      <td mat-cell *matCellDef="let c"> {{c.deliveryDate | date}} </td>
                    </ng-container>
                    <ng-container matColumnDef="details">
                      <th mat-header-cell *matHeaderCellDef> Details </th>
                      <td mat-cell *matCellDef="let c"> {{c.details}} </td>
                    </ng-container>
                    <ng-container matColumnDef="amount">
                      <th mat-header-cell *matHeaderCellDef> Amount </th>
                      <td mat-cell *matCellDef="let c"> {{c.charge}} </td>
                    </ng-container>
                    <tr mat-header-row *matHeaderRowDef="['date', 'details', 'amount']"></tr>
                    <tr mat-row *matRowDef="let row; columns: ['date', 'details', 'amount'];"></tr>
                  </table>
                </mat-tab>
                <mat-tab label="Payments">
                  <table mat-table [dataSource]="installments" class="full-width">
                    <ng-container matColumnDef="date">
                      <th mat-header-cell *matHeaderCellDef> Date </th>
                      <td mat-cell *matCellDef="let i"> {{i.paymentDay | date}} </td>
                    </ng-container>
                    <ng-container matColumnDef="amount">
                      <th mat-header-cell *matHeaderCellDef> Amount </th>
                      <td mat-cell *matCellDef="let i"> {{i.amount}} </td>
                    </ng-container>
                    <tr mat-header-row *matHeaderRowDef="['date', 'amount']"></tr>
                    <tr mat-row *matRowDef="let row; columns: ['date', 'amount'];"></tr>
                  </table>
                </mat-tab>
              </mat-tabs>
            </mat-card-content>
          </mat-card>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .accounting-container { padding: 20px; }
    .search-section { margin-bottom: 20px; }
    .search-field { width: 100%; max-width: 400px; }
    .selected { background-color: #e0e0e0; cursor: default; }
    .patient-list mat-list-item { cursor: pointer; }
    .balance { font-size: 0.8em; }
    .negative { color: red; }
    .full-width { width: 100%; }
    .actions { margin-left: auto; display: flex; gap: 8px; align-items: center; }
    .form-container {
      padding: 20px;
      background-color: #f5f5f5;
      margin-bottom: 20px;
      border-radius: 4px;
    }
    .form-actions { margin-top: 10px; display: flex; justify-content: flex-end; gap: 10px; }
    .main-content { gap: 20px; }
  `]
})
export class AccountingComponent implements OnInit {
  patients: PatientBasicInfo[] = [];
  selectedPatientId: string = null;
  selectedPatientName: string = '';
  charges: Charge[] = [];
  installments: Installment[] = [];

  showAddCharge = false;
  showAddInstallment = false;

  newCharge: Partial<Charge> = { charge: 0, details: '', deliveryDate: new Date() };
  newInstallment: Partial<Installment> = { amount: 0, paymentDay: new Date() };

  constructor(private patientService: PatientService, private http: HttpClient) {}

  ngOnInit() {}

  onSearch(event: Event) {
    const term = (event.target as HTMLInputElement).value;
    if (term.trim()) {
      this.patientService.getPatients(term).subscribe(data => this.patients = data);
    } else {
      this.patients = [];
    }
  }

  selectPatient(patient: PatientBasicInfo) {
    this.selectedPatientId = patient.id;
    this.selectedPatientName = patient.fullName;
    this.showAddCharge = false;
    this.showAddInstallment = false;
    this.loadHistory();
  }

  loadHistory() {
    this.http.get<any>('/patient/' + this.selectedPatientId + '/account/charges')
      .pipe(map(data => data.content || data))
      .subscribe(data => this.charges = data);

    this.http.get<any>('/patient/' + this.selectedPatientId + '/account/installments')
      .pipe(map(data => data.content || data))
      .subscribe(data => this.installments = data);
  }

  toggleAddCharge() {
    this.showAddCharge = !this.showAddCharge;
    this.showAddInstallment = false;
  }

  toggleAddInstallment() {
    this.showAddInstallment = !this.showAddInstallment;
    this.showAddCharge = false;
  }

  addCharge() {
    this.http.post('/patient/' + this.selectedPatientId + '/account/charges', this.newCharge)
      .subscribe(() => {
        this.showAddCharge = false;
        this.newCharge = { charge: 0, details: '', deliveryDate: new Date() };
        this.loadHistory();
        this.refreshPatientBalance();
      });
  }

  addInstallment() {
    this.http.post('/patient/' + this.selectedPatientId + '/account/installments', this.newInstallment)
      .subscribe(() => {
        this.showAddInstallment = false;
        this.newInstallment = { amount: 0, paymentDay: new Date() };
        this.loadHistory();
        this.refreshPatientBalance();
      });
  }

  refreshPatientBalance() {
    const patient = this.patients.find(p => p.id === this.selectedPatientId);
    if (patient) {
      this.http.get<any>('/patient/' + this.selectedPatientId + '/balance')
        .subscribe(data => (patient as any).balance = data.balance);
    }
  }
}
