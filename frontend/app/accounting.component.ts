import { Component, OnInit } from '@angular/core';
import { PatientService } from './patient.service';
import { PatientBasicInfo } from './patientbasic';
import { Charge } from './charge';
import { Installment } from './installment';

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
                    <mat-label>Practice (Optional)</mat-label>
                    <mat-select [(ngModel)]="newInstallment.practiceId">
                      <mat-option [value]="null">None</mat-option>
                      <mat-option *ngFor="let p of practices" [value]="p.id">
                        {{p.deliveryDate | date:'shortDate'}} - {{p.code}} ($ {{p.price}})
                      </mat-option>
                    </mat-select>
                  </mat-form-field>
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

              <mat-tab-group class="history-tabs">
                <mat-tab label="Charges">
                  <div class="history-list">
                    <table class="history-table">
                      <thead>
                        <tr>
                          <th>Date</th>
                          <th>Details</th>
                          <th>Amount</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr *ngFor="let c of charges">
                          <td>{{c.deliveryDate | date}}</td>
                          <td>{{c.details}}</td>
                          <td>{{c.charge}}</td>
                        </tr>
                        <tr *ngIf="charges.length === 0">
                          <td colspan="3" class="empty-msg">No charges found</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </mat-tab>
                <mat-tab label="Payments">
                  <div class="history-list">
                    <table class="history-table">
                      <thead>
                        <tr>
                          <th>Date</th>
                          <th>Amount</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr *ngFor="let i of installments">
                          <td>{{i.paymentDay | date}}</td>
                          <td>{{i.amount}}</td>
                        </tr>
                        <tr *ngIf="installments.length === 0">
                          <td colspan="2" class="empty-msg">No payments found</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </mat-tab>
              </mat-tab-group>
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
    .actions { margin-left: auto; display: flex; gap: 8px; align-items: center; }
    .form-container {
      padding: 20px;
      background-color: #f5f5f5;
      margin-bottom: 20px;
      border-radius: 4px;
    }
    .form-actions { margin-top: 10px; display: flex; justify-content: flex-end; gap: 10px; }
    .main-content { gap: 20px; }
    .history-tabs { margin-top: 10px; }
    .history-list { margin-top: 10px; min-height: 100px; }
    .history-table { width: 100%; border-collapse: collapse; }
    .history-table th, .history-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
    .history-table th { background-color: #f8f9fa; }
    .empty-msg { text-align: center; color: #666; font-style: italic; }
  `]
})
export class AccountingComponent implements OnInit {
  patients: PatientBasicInfo[] = [];
  selectedPatientId: string = null;
  selectedPatientName: string = '';
  charges: Charge[] = [];
  installments: Installment[] = [];
  practices: any[] = [];

  showAddCharge = false;
  showAddInstallment = false;

  newCharge: Partial<Charge> = { charge: 0, details: '', deliveryDate: new Date() };
  newInstallment: Partial<Installment> = { amount: 0, paymentDay: new Date() };

  constructor(private patientService: PatientService) {}

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
    this.patientService.getCharges(this.selectedPatientId)
      .subscribe(data => this.charges = data);

    this.patientService.getInstallments(this.selectedPatientId)
      .subscribe(data => this.installments = data);

    this.patientService.getPatientPractices(this.selectedPatientId)
      .subscribe(data => {
        const content = (data as any).content || data;
        this.practices = content.filter(p => p.done);
      });
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
    this.patientService.addCharge(this.selectedPatientId, this.newCharge)
      .subscribe(() => {
        this.showAddCharge = false;
        this.newCharge = { charge: 0, details: '', deliveryDate: new Date() };
        this.loadHistory();
        this.refreshPatientBalance();
      });
  }

  addInstallment() {
    if (this.newInstallment.practiceId) {
      const associatedCharge = this.charges.find(c => c.practiceId === this.newInstallment.practiceId);
      if (associatedCharge) {
        this.newInstallment.chargeId = associatedCharge.id;
      }
    }
    this.patientService.addInstallment(this.selectedPatientId, this.newInstallment)
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
      this.patientService.getBalance(this.selectedPatientId)
        .subscribe(balance => (patient as any).balance = balance);
    }
  }
}
