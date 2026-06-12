import { Component, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { OnInit } from '@angular/core';

import { PatientDetails } from './patient-details.component'
import { PatientBasicInfo } from './patientbasic'
import { PatientService } from './patient.service'


@Component({
  selector: 'patient-select',

  template: `
	<div layout="row">
		<div style="background-color:#EFF2F5" flex="30">
			<div *ngIf="patients">
					<table border="1" width="100%">

				<thead>
				<tr>
						<th>Name</th>
						<th>Address</th>
						<th>Balance</th>
				</tr>
				</thead>

				<tbody>

				<tr *ngFor= "let patient of patients"
						[class.selected]="patient === selectedPatient"
						(click)="onSelect(patient)">
					<td>
					<span class="badge">{{patient.fullName}}</span>
					</td>

					<td>
					{{patient.address}}
					</td>

					<td>
					<span [class.debit]="patient.balance < 0">  {{patient.balance}} </span>
					</td>

				</tr>
				</tbody>
				</table>
			</div>
		</div>
		<div style="background-color:#EFF2F5" flex="70">
			<patient-details  [patientId]="selectedPatient"> </patient-details>
		</div>
	</div>

  `,
  styles: [`
    .selected {
      background-color: #CFD8DC !important;
      color: white;
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

	@Input() searchQuery: string = '';

	constructor(private patientService : PatientService) {}

	ngOnInit(): void {
		this.searchPatients(this.searchQuery);
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (changes['searchQuery']) {
			this.searchPatients(this.searchQuery);
		}
	}

	private searchPatients(query: string): void {
		this.patientService.getPatients(query)
			.subscribe({
				next: patients => this.patients = patients,
				error: error => this.errorMessage = <any>error
			});
	}

	onSelect(patient: PatientBasicInfo): void {
		console.log("Selected patient: " + patient.fullName);
		this.selectedPatient = patient.id;
	}

	patients : PatientBasicInfo[];
	patientSearchText : string;
	selectedPatient : string;

	errorMessage: string;
}
