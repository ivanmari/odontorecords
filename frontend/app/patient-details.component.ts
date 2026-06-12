import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
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
		<div class="actions">
			<button mat-raised-button color="accent" (click)="reset()">Add New</button>
		</div>

		<div *ngIf="patientFull" class="patient-form">
			<mat-card>
				<mat-card-header>
					<mat-card-title>{{isUpdate ? 'Edit Patient' : 'New Patient'}}</mat-card-title>
					<mat-card-subtitle *ngIf="isUpdate">{{patientFull.firstName}} {{patientFull.lastName}}</mat-card-subtitle>
				</mat-card-header>
				<mat-card-content>
					<div layout="column" class="form-container">
						<div layout="row" layout-gap="10px">
							<mat-form-field appearance="fill" flex="50">
								<mat-label>First Name</mat-label>
								<input matInput [(ngModel)]="patientFull.firstName" required placeholder="First Name">
							</mat-form-field>
							<mat-form-field appearance="fill" flex="50">
								<mat-label>Last Name</mat-label>
								<input matInput [(ngModel)]="patientFull.lastName" required placeholder="Last Name">
							</mat-form-field>
						</div>

						<div layout="row" layout-gap="10px">
							<mat-form-field appearance="fill" flex="50">
								<mat-label>DNI</mat-label>
								<input matInput type="number" [(ngModel)]="patientFull.dni" required placeholder="DNI">
							</mat-form-field>
							<mat-form-field appearance="fill" flex="50">
								<mat-label>Birthday</mat-label>
								<input matInput [matDatepicker]="picker" [(ngModel)]="patientFull.birthday" required placeholder="Choose a date">
								<mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
								<mat-datepicker #picker></mat-datepicker>
							</mat-form-field>
						</div>

						<div layout="row" layout-gap="10px">
							<mat-form-field appearance="fill" flex="50">
								<mat-label>Social Security Org</mat-label>
								<input matInput [(ngModel)]="patientFull.socialSecOrg" placeholder="Social Security Org">
							</mat-form-field>
							<mat-form-field appearance="fill" flex="50">
								<mat-label>Social Security ID</mat-label>
								<input matInput [(ngModel)]="patientFull.socialId" placeholder="Social Security ID">
							</mat-form-field>
						</div>

						<div layout="row" layout-gap="10px">
							<mat-form-field appearance="fill" flex="33">
								<mat-label>Gender</mat-label>
								<input matInput [(ngModel)]="patientFull.gender" placeholder="Gender">
							</mat-form-field>
							<mat-form-field appearance="fill" flex="33">
								<mat-label>Phone</mat-label>
								<input matInput [(ngModel)]="patientFull.phone" placeholder="Phone">
							</mat-form-field>
							<mat-form-field appearance="fill" flex="33">
								<mat-label>City</mat-label>
								<input matInput [(ngModel)]="patientFull.city" placeholder="City">
							</mat-form-field>
						</div>

						<div layout="row" layout-gap="10px">
							<mat-form-field appearance="fill" flex="40">
								<mat-label>Street</mat-label>
								<input matInput [(ngModel)]="patientFull.street" placeholder="Street">
							</mat-form-field>
							<mat-form-field appearance="fill" flex="30">
								<mat-label>Number</mat-label>
								<input matInput [(ngModel)]="patientFull.streetNum" placeholder="Number">
							</mat-form-field>
							<mat-form-field appearance="fill" flex="30">
								<mat-label>Apartment</mat-label>
								<input matInput [(ngModel)]="patientFull.apartment" placeholder="Apartment">
							</mat-form-field>
						</div>
					</div>
				</mat-card-content>
				<mat-card-actions align="end">
					<button mat-button color="primary" (click)="processForm()">{{isUpdate ? 'Update' : 'Save'}}</button>
				</mat-card-actions>
			</mat-card>

			<div *ngIf="patientFull.id" class="mouth-view">
				<mouth [patientId]="patientFull.id"></mouth>
			</div>
		</div>
		
`,
styles: [`
    .patient-form {
      padding: 10px;
    }
	.form-container {
		padding-top: 10px;
	}
	.actions {
		padding: 10px;
	}
	.mouth-view {
		margin-top: 20px;
	}
	`],
  providers: [PatientService]
})
export class PatientDetails implements OnChanges
{
	@Input()
	patientId: string;
	
	patientFull : Patient;
	
	isUpdate: boolean = true;
	updateError: boolean = false;
	errorMessage: string = "";
	
	constructor(private patientService : PatientService, private snackBar: MatSnackBar) {}
	
	getFullPatient(patientId : string){
		if(patientId)
		{
			console.log("Details Comp., selected id: " + patientId);
			this.patientService.getPatient(patientId)
							.subscribe(
							p => this.patientFull = p,
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
			if(propName === "patientId" && changes[propName]) {
				let patientId = changes[propName].currentValue;
				if (patientId) {
					this.getFullPatient(patientId);
					this.isUpdate = true;
				}
			}
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
	}
	
}