import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges, HostListener } from '@angular/core';
import { OnInit } from '@angular/core';

import { PatientBasicInfo } from './patientbasic'
import { PatientService } from './patient.service'


@Component({
  selector: 'patient-select',

  template: `
	<div layout="row" layout-gap="20px">
		<div style="background-color:#fff" flex="60" class="list-container">
			<div *ngIf="patients && patients.length > 0">
				<table class="patient-table">
					<thead>
						<tr>
							<th>Name</th>
							<th>Address</th>
							<th>Balance</th>
						</tr>
					</thead>
					<tbody>
						<tr *ngFor="let patient of patients; let i = index"
							[class.highlighted]="i === highlightedIndex"
							[class.selected]="patient === selectedPatient"
							(click)="onSelect(patient, i)"
							(dblclick)="onConfirm(patient)">
							<td>
								<span class="patient-name">{{patient.fullName}}</span>
							</td>
							<td>{{patient.address}}</td>
							<td>
								<span [class.debit]="patient.balance < 0" [class.credit]="patient.balance >= 0">
									{{patient.balance | currency}}
								</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div *ngIf="!patients || patients.length === 0" class="no-results">
				<mat-icon>search_off</mat-icon>
				<p>No patients found matching your search.</p>
			</div>
		</div>

		<div flex="40">
			<mat-card *ngIf="selectedPatient" class="preview-card">
				<mat-card-header>
					<mat-card-title>Patient Preview</mat-card-title>
					<mat-card-subtitle>Minimal Information</mat-card-subtitle>
				</mat-card-header>
				<mat-card-content>
					<div class="preview-info">
						<div class="info-row">
							<span class="label">Full Name:</span>
							<span class="value">{{selectedPatient.fullName}}</span>
						</div>
						<div class="info-row">
							<span class="label">Address:</span>
							<span class="value">{{selectedPatient.address}}</span>
						</div>
						<div class="info-row">
							<span class="label">Social Security:</span>
							<span class="value">{{selectedPatient.socialSec || 'N/A'}}</span>
						</div>
						<div class="info-row">
							<span class="label">Balance:</span>
							<span class="value" [class.debit]="selectedPatient.balance < 0">
								{{selectedPatient.balance | currency}}
							</span>
						</div>
					</div>
				</mat-card-content>
				<mat-card-actions align="end">
					<button mat-raised-button color="primary" (click)="onConfirm(selectedPatient)">
						VIEW FULL PROFILE
					</button>
				</mat-card-actions>
			</mat-card>
		</div>
	</div>

  `,
  styles: [`
    .list-container {
      border: 1px solid #e0e0e0;
      border-radius: 4px;
      max-height: 70vh;
      overflow-y: auto;
    }
    .patient-table {
      width: 100%;
      border-collapse: collapse;
    }
    .patient-table th {
      text-align: left;
      padding: 12px;
      background-color: #f5f5f5;
      border-bottom: 2px solid #e0e0e0;
    }
    .patient-table td {
      padding: 12px;
      border-bottom: 1px solid #e0e0e0;
    }
    .patient-table tr:hover {
      background-color: #f0f0f0;
      cursor: pointer;
    }
    .highlighted {
      background-color: #e3f2fd !important;
    }
    .selected {
      background-color: #bbdefb !important;
      font-weight: bold;
    }
	.credit {
	  color: green;
	}
	.debit {
		color: red;
	}
    .patients {
      margin: 0 0 2em 0;
      list-style-type: none;
      padding: 0;
      width: 15em;
    }
    .patients li {
      cursor: pointer;
      position: relative;
      left: 0;
      background-color: #EEE;
      margin: .5em;
      padding: .3em 0;
      height: 1.6em;
      border-radius: 4px;
    }
    .patients li.selected:hover {
      background-color: #BBD8DC !important;
      color: white;
    }
    .patients li:hover {
      color: #607D8B;
      background-color: #DDD;
      left: .1em;
    }
    .patients .text {
      position: relative;
      top: -3px;
    }
    .patients .badge {
      display: inline-block;
      font-size: small;
      color: white;
      padding: 0.8em 0.7em 0 0.7em;
      background-color: #607D8B;
      line-height: 1em;
      position: relative;
      left: -1px;
      top: -4px;
      height: 1.8em;
      margin-right: .8em;
      border-radius: 4px 0 0 4px;
    }
  `],

  providers: [PatientService]

})

export class PatientSelect implements OnInit, OnChanges {

	loadRequest = new EventEmitter<number>();

	@Input() searchTerm: string = '';
	@Output() confirmed = new EventEmitter<string>();

	patients: PatientBasicInfo[] = [];
	selectedPatient: PatientBasicInfo | null = null;
	highlightedIndex: number = -1;
	errorMessage: string;

	constructor(private patientService : PatientService) {}

	ngOnInit(): void {
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (changes['searchTerm']) {
			this.searchPatients(this.searchTerm);
		}
	}

	@HostListener('window:keydown', ['$event'])
	handleKeyboardEvent(event: KeyboardEvent) {
		if (!this.patients || this.patients.length === 0) return;

		if (event.key === 'ArrowDown') {
			event.preventDefault();
			this.highlightedIndex = Math.min(this.highlightedIndex + 1, this.patients.length - 1);
			this.onSelect(this.patients[this.highlightedIndex], this.highlightedIndex);
		} else if (event.key === 'ArrowUp') {
			event.preventDefault();
			this.highlightedIndex = Math.max(this.highlightedIndex - 1, 0);
			this.onSelect(this.patients[this.highlightedIndex], this.highlightedIndex);
		} else if (event.key === 'Enter') {
			if (this.highlightedIndex >= 0) {
				this.onConfirm(this.patients[this.highlightedIndex]);
			}
		}
	}

	onSelect(patient: PatientBasicInfo, index: number): void {
		console.log("Selected patient: " + patient.fullName);
		this.selectedPatient = patient;
		this.highlightedIndex = index;
	}

	onConfirm(patient: PatientBasicInfo): void {
		if (patient) {
			this.confirmed.emit(patient.id);
		}
	}

	private searchPatients(term: string) {
		if (!term || term.trim() === '') {
			this.patients = [];
			this.selectedPatient = null;
			this.highlightedIndex = -1;
			return;
		}
		this.patientService.getPatients(term)
						.subscribe(
						patients => {
							this.patients = patients;
							if (this.patients && this.patients.length > 0) {
								this.highlightedIndex = 0;
								this.onSelect(this.patients[0], 0);
							} else {
								this.selectedPatient = null;
								this.highlightedIndex = -1;
							}
						},
						error => this.errorMessage = <any>error);
	}
}
