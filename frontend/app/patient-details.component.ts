import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Patient } from './patient'
import { PatientService } from './patient.service'
import { PatientBasicInfo } from './patientbasic'
// removed deprecated @angular/http import
import { Mouth } from './mouth.component'

@Component({
	selector:'patient-details',
	template: `
		<button type="button" (click)="reset()">Add New</button>
		<div *ngIf="patientFull">
		{{patientFull.firstName}} {{patientFull.lastName}}
			  <table>
				<tr>	<td align="right">id Only for debug: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.id" placeholder="Debug"/></td>
				</tr>			
				<tr>	<td align="right">Last Name: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.lastName" placeholder="lastName"/></td>
				</tr>
				<tr>	<td align="right">First Name: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.firstName" placeholder="firstName"/></td>
				</tr>
				<tr>	<td align="right">Social Security Org: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.socialSecOrg" placeholder="socialSecOrg"/></td>
				</tr>
				<tr>	<td align="right">Social Security ID: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.socialId" placeholder="socialSecId"/></td>
				</tr>
				<tr>	<td align="right">Gender: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.gender" placeholder="gender"/></td>
				</tr>
				<tr>	<td align="right">Birthday: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.birthday" placeholder="birthday"/></td>
				</tr>
				<tr>	<td align="right">Phone: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.phone" placeholder="phone"/></td>
				</tr>
				<tr>	<td align="right">City: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.city" placeholder="city"/></td>
				</tr>
				<tr>	<td align="right">Street: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.street" placeholder="street"/></td>
				</tr>
				<tr>	<td align="right">Address: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.streetNum" placeholder="Number"/></td>
				</tr>
				<tr>	<td align="right">Apartment: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="patientFull.apartment" placeholder="apartment"/></td>
				</tr>
				</table>
				<button type="button" (click)="processForm()">{{isUpdate ? 'Update' : 'Save'}}</button>
				
				<div *ngIf="updateStatusMsg">
					<p><span [class.error]="updateError">  {{updateStatusMsg}} </span></p>
				</div>
				
				
				<div *ngIf="patientFull.id">
					<mouth [patientId]="patientFull.id"></mouth>
				</div>
		</div>
		
`,
styles: [`
    .error {
      background-color: #CFD8DC !important; 
      color: red;
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
	updateStatusMsg : string = "";
	updateStatus: number = 0;
	errorMessage: string = "";
	
	constructor(private patientService : PatientService) {}
	
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
				
				this.getFullPatient(patientId);
				this.updateStatusMsg = "";
				this.isUpdate = true;
			}
		}
	}
	
	onDetailsChange() {
		this.updateStatusMsg = "";
	}
	
	processForm() {
		if(this.patientFull)
		{
			if(this.isUpdate)
			{
				console.log("Updating patient");
				let res: number;
				
					this.patientService.updatePatient(this.patientFull).subscribe(err => this.updateError = !err);
				
				
				if(this.updateError)
				{
					this.updateStatusMsg = "An error occurred, update failed";
				}
				else
				{
					this.updateStatusMsg = "Updated successfuly";
				}
			}
			else
			{
				console.log("Saving new patient");
				this.patientService.addPatient(this.patientFull).subscribe(err => this.updateError = !err);
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