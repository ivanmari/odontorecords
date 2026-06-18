import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Patient } from './patient'
import { PatientService } from './patient.service'
import { PatientBasicInfo } from './patientbasic'
// removed deprecated @angular/http import
import { Mouth } from './mouth.component'

@Component({
	selector:'patient-details',
	template: `
		<div *ngIf="patientFull" class="profile-container">
			<!-- Patient Header -->
			<mat-card class="header-card">
				<div layout="row" layout-align="space-between center">
					<div flex="70">
						<button mat-icon-button (click)="back.emit()" title="Back to Search">
							<mat-icon>arrow_back</mat-icon>
						</button>
						<h1 class="patient-name" style="display: inline-block; vertical-align: middle; margin-left: 8px;">
							{{isUpdate ? patientFull.firstName + ' ' + patientFull.lastName : 'New Patient Registration'}}
						</h1>
						<div class="patient-meta" style="margin-left: 48px;" *ngIf="isUpdate">
							<span><strong>MRN:</strong> {{patientFull.dni}}</span>
							<span class="meta-separator">|</span>
							<span><strong>Visit Type:</strong> {{patientFull.visitType || 'Regular'}}</span>
						</div>
					</div>
					<div flex="30" style="text-align: right;">
						<button mat-stroked-button color="primary" (click)="back.emit()">
							<mat-icon>search</mat-icon> Search Patients
						</button>
					</div>
				</div>
			</mat-card>

			<mat-tab-group (selectedHeaderChange)="onTabChange($event)" [(selectedIndex)]="selectedTabIndex">
				<!-- VIEW MODE TAB -->
				<mat-tab label="Patient Profile">
					<div layout="row" layout-gap="20px" class="tab-content">
						<!-- Main Column -->
						<div flex="70" layout="column" layout-gap="20px">
							<mat-card>
								<mat-card-header>
									<mat-card-title>Odontograme</mat-card-title>
								</mat-card-header>
								<mat-card-content>
									<div class="mouth-view">
										<mouth [patientId]="patientFull.id" [editMode]="false"></mouth>
									</div>
								</mat-card-content>
							</mat-card>

							<mat-card>
								<mat-card-header>
									<mat-card-title>Clinical History</mat-card-title>
								</mat-card-header>
								<mat-card-content>
									<table border="1" width="100%" class="practices-table">
										<thead>
											<tr>
												<th>Date</th>
												<th>Code</th>
												<th>Price</th>
												<th>Comments</th>
											</tr>
										</thead>
										<tbody>
											<tr *ngFor="let p of allPractices">
												<td>{{p.deliveryDate | date}}</td>
												<td>{{p.code}}</td>
												<td>{{p.price | currency}}</td>
												<td>{{p.comments}}</td>
											</tr>
											<tr *ngIf="allPractices.length === 0">
												<td colspan="4" style="text-align: center; padding: 20px;">No practices recorded.</td>
											</tr>
										</tbody>
									</table>
								</mat-card-content>
							</mat-card>
						</div>

						<!-- Right Panel -->
						<div flex="30" layout="column" layout-gap="20px">
							<mat-card class="balance-card" [class.negative]="patientFull.balance < 0">
								<mat-card-header>
									<mat-card-title>Account Balance</mat-card-title>
								</mat-card-header>
								<mat-card-content class="balance-content">
									<div class="balance-amount">{{patientFull.balance | currency}}</div>
								</mat-card-content>
							</mat-card>

							<mat-card>
								<mat-card-header>
									<mat-card-title>Recent Visits</mat-card-title>
								</mat-card-header>
								<mat-card-content>
									<mat-list>
										<mat-list-item *ngFor="let visit of patientFull.recentVisits">
											<mat-icon matListIcon>calendar_today</mat-icon>
											<div matLine><strong>{{visit.date | date}}</strong></div>
											<div matLine>{{visit.reason}}</div>
										</mat-list-item>
									</mat-list>
								</mat-card-content>
							</mat-card>
						</div>
					</div>
				</mat-tab>

				<!-- EDIT MODE TAB -->
				<mat-tab label="Edit Records">
					<div layout="column" layout-gap="20px" class="tab-content">
						<!-- Odontograme Edit Section -->
						<mat-card>
							<mat-card-header>
								<div layout="row" layout-align="space-between center" flex="100">
									<mat-card-title>Interactive Odontograme</mat-card-title>
									<div class="edit-hint" style="color: #f44336; font-weight: bold;">
										<mat-icon style="vertical-align: middle;">touch_app</mat-icon> Click a tooth to modify clinical status
									</div>
								</div>
							</mat-card-header>
							<mat-card-content>
								<div class="mouth-view edit-view">
									<mouth [patientId]="patientFull.id" [editMode]="true"></mouth>
								</div>
							</mat-card-content>
						</mat-card>

						<!-- Demographic Data Form -->
						<mat-card class="patient-form">
							<mat-card-header>
								<mat-card-title>{{isUpdate ? 'Update Patient Demographics' : 'New Patient Registration'}}</mat-card-title>
							</mat-card-header>
							<mat-card-content>
								<div layout="column" class="form-container">
									<div layout="row" layout-gap="10px">
										<mat-form-field appearance="fill" flex="50">
											<mat-label>First Name</mat-label>
											<input matInput [(ngModel)]="patientFull.firstName" required>
										</mat-form-field>
										<mat-form-field appearance="fill" flex="50">
											<mat-label>Last Name</mat-label>
											<input matInput [(ngModel)]="patientFull.lastName" required>
										</mat-form-field>
									</div>

									<div layout="row" layout-gap="10px">
										<mat-form-field appearance="fill" flex="50">
											<mat-label>DNI</mat-label>
											<input matInput type="number" [(ngModel)]="patientFull.dni" required>
										</mat-form-field>
										<mat-form-field appearance="fill" flex="50">
											<mat-label>Birthday</mat-label>
											<input matInput [matDatepicker]="picker" [(ngModel)]="patientFull.birthday" required>
											<mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
											<mat-datepicker #picker></mat-datepicker>
										</mat-form-field>
									</div>

									<div layout="row" layout-gap="10px">
										<mat-form-field appearance="fill" flex="50">
											<mat-label>Social Security Org</mat-label>
											<input matInput [(ngModel)]="patientFull.socialSecOrg">
										</mat-form-field>
										<mat-form-field appearance="fill" flex="50">
											<mat-label>Social Security ID</mat-label>
											<input matInput [(ngModel)]="patientFull.socialId">
										</mat-form-field>
									</div>

									<div layout="row" layout-gap="10px">
										<mat-form-field appearance="fill" flex="33">
											<mat-label>Gender</mat-label>
											<input matInput [(ngModel)]="patientFull.gender">
										</mat-form-field>
										<mat-form-field appearance="fill" flex="33">
											<mat-label>Phone</mat-label>
											<input matInput [(ngModel)]="patientFull.phone">
										</mat-form-field>
										<mat-form-field appearance="fill" flex="33">
											<mat-label>City</mat-label>
											<input matInput [(ngModel)]="patientFull.city">
										</mat-form-field>
									</div>

									<div layout="row" layout-gap="10px">
										<mat-form-field appearance="fill" flex="40">
											<mat-label>Street</mat-label>
											<input matInput [(ngModel)]="patientFull.street">
										</mat-form-field>
										<mat-form-field appearance="fill" flex="30">
											<mat-label>Number</mat-label>
											<input matInput [(ngModel)]="patientFull.streetNum">
										</mat-form-field>
										<mat-form-field appearance="fill" flex="30">
											<mat-label>Apartment</mat-label>
											<input matInput [(ngModel)]="patientFull.apartment">
										</mat-form-field>
									</div>

									<div layout="row" layout-gap="10px">
										<mat-form-field appearance="fill" flex="50">
											<mat-label>Visit Type</mat-label>
											<input matInput [(ngModel)]="patientFull.visitType">
										</mat-form-field>
										<mat-form-field appearance="fill" flex="50">
											<mat-label>Reason for Visit</mat-label>
											<input matInput [(ngModel)]="patientFull.reasonVisit">
										</mat-form-field>
									</div>
								</div>
							</mat-card-content>
							<mat-card-actions align="end">
								<button mat-raised-button color="primary" (click)="processForm()">{{isUpdate ? 'Update Patient' : 'Save Patient'}}</button>
								<button mat-button (click)="selectedTabIndex = 0">Cancel</button>
							</mat-card-actions>
						</mat-card>
					</div>
				</mat-tab>
			</mat-tab-group>
		</div>
		
`,
styles: [`
	.profile-container {
		padding: 20px;
		background-color: #f5f5f5;
	}
	.header-card {
		margin-bottom: 20px;
		padding: 20px;
	}
	.patient-name {
		margin: 0;
		font-size: 24px;
		color: #3f51b5;
	}
	.patient-meta {
		margin-top: 10px;
		color: #666;
	}
	.meta-separator {
		margin: 0 10px;
		color: #ccc;
	}
	.tab-content {
		padding: 20px 0;
	}
	.dashboard-layout {
		margin-top: 20px;
	}
    .patient-form {
      padding: 0;
	  margin-bottom: 20px;
    }
	.form-container {
		padding-top: 10px;
	}
	.actions {
		padding: 10px;
	}
	.mouth-view {
		margin-top: 10px;
		overflow: hidden;
	}
	.balance-card.negative .balance-amount {
		color: #f44336;
	}
	.balance-amount {
		font-size: 32px;
		font-weight: bold;
		text-align: center;
		padding: 20px 0;
		color: #4caf50;
	}
	.practices-table {
		border-collapse: collapse;
		margin-top: 10px;
	}
	.practices-table th, .practices-table td {
		padding: 12px;
		text-align: left;
		border: 1px solid #eee;
	}
	.practices-table th {
		background-color: #fafafa;
	}
	`],
  providers: [PatientService]
})
export class PatientDetails implements OnChanges
{
	@Input()
	patientId: string;

	@Output()
	back = new EventEmitter<void>();
	
	patientFull : Patient;
	
	isUpdate: boolean = true;
	isEditing: boolean = false;
	selectedTabIndex: number = 0;
	updateError: boolean = false;
	errorMessage: string = "";
	allPractices: any[] = [];
	
	constructor(private patientService : PatientService, private snackBar: MatSnackBar) {}
	
	onTabChange(event: any) {
		// Sync isEditing with tab selection
		this.isEditing = (this.selectedTabIndex === 1);
	}

	getFullPatient(patientId : string){
		if(patientId)
		{
			console.log("Details Comp., selected id: " + patientId);
			this.patientService.getPatient(patientId)
							.subscribe(
							p => {
								this.patientFull = p;
								this.loadMockData();
								this.loadPractices();
							},
							error => this.errorMessage = <any>error);
		}
		else
		{
			console.log("getFullPatient: patientId undefined, call ignored");
		}
	}
	
	ngOnChanges(changes : SimpleChanges) {
		for(let propName in changes)
		{
			if(propName === "patientId") {
				let patientId = changes[propName].currentValue;
				if (patientId) {
					this.getFullPatient(patientId);
					this.isUpdate = true;
					this.isEditing = false;
					this.selectedTabIndex = 0;
				} else {
					// If patientId is null/undefined, it's a new patient
					this.reset();
				}
			}
		}
	}

	loadMockData() {
		if (this.patientFull) {
			// Initialize mock data for fields not yet supported by backend
			if (!this.patientFull.recentVisits) {
				this.patientFull.recentVisits = [
					{ date: new Date(2024, 4, 10).toISOString(), reason: 'Initial Check-up' },
					{ date: new Date(2024, 5, 15).toISOString(), reason: 'Cleaning' }
				];
			}
			if (!this.patientFull.visitType) this.patientFull.visitType = 'Regular';
			if (!this.patientFull.reasonVisit) this.patientFull.reasonVisit = 'Annual Check-up';
		}
	}

	loadPractices() {
		if (this.patientId) {
			this.patientService.getPatientPractices(this.patientId).subscribe({
				next: (practices) => {
					this.allPractices = practices || [];
				},
				error: (err) => {
					console.error("Error loading patient practices", err);
					this.allPractices = [];
				}
			});
		}
	}
	
	private isValid(): boolean {
		if (!this.patientFull) return false;
		return !!(this.patientFull.firstName &&
				  this.patientFull.lastName &&
				  this.patientFull.dni &&
				  this.patientFull.birthday);
	}

	processForm() {
		if(this.patientFull)
		{
			if (!this.isValid()) {
				this.snackBar.open("Please fill all mandatory fields (Name, Surname, DNI, Birthday)", "Close", { duration: 3000 });
				return;
			}

			// Convert Date back to string if it's a Date object (from datepicker)
			if (this.patientFull.birthday instanceof Date) {
				this.patientFull.birthday = (this.patientFull.birthday as Date).toISOString();
			}

			if(this.isUpdate)
			{
				console.log("Updating patient");
				this.patientService.updatePatient(this.patientFull).subscribe({
					next: (success) => {
						if (success) {
							this.snackBar.open("Patient updated successfully", "Close", { duration: 3000 });
						} else {
							this.snackBar.open("Error: Could not update patient", "Close", { duration: 3000 });
						}
					},
					error: (err) => {
						this.snackBar.open("An error occurred during update", "Close", { duration: 3000 });
						console.error(err);
					}
				});
			}
			else
			{
				console.log("Saving new patient");
				this.patientService.addPatient(this.patientFull).subscribe({
					next: (success) => {
						if (success) {
							this.snackBar.open("Patient saved successfully", "Close", { duration: 3000 });
							this.isUpdate = true; // Switch to update mode after save
						} else {
							this.snackBar.open("Error: Could not save patient", "Close", { duration: 3000 });
						}
					},
					error: (err) => {
						this.snackBar.open("An error occurred during save", "Close", { duration: 3000 });
						console.error(err);
					}
				});
			}
		}
		else
		{
			console.log("processForm: patientFull is null");
		}
	}
	
	reset(){
		this.patientFull = new Patient();
		this.isUpdate = false;
		this.allPractices = [];
	}
	
}