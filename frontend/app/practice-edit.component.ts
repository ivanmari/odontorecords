import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PatientService } from './patient.service'
import { Practice } from './practice'

@Component({
	selector:'practice-edit',
	template: `
	
	<div *ngIf="practice">
		<select class="form-control" required
          [(ngModel)]="practice.face">
		<option value='Distal'> Distal </option>
		<option value='Mesial'> Mesial </option>
		<option value='Vestibular'> Vestibular </option>
		<option value='Lingual'> Lingual </option>
		<option value='Oclusal'> Oclusal </option>
	</select>
	{{practice.face}}
		<table>
				<tr>	<td align="right">code: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="practice.code" placeholder="code"/></td>
				</tr>			
				<tr>	<td align="right">Date: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="practice.deliveryDate" placeholder="date"/></td>
				</tr>
				<tr>	<td align="right">Price: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="practice.price" placeholder="price"/></td>
				</tr>
				<tr>	<td align="right">Comments: </td>
					<td align="left"><input (keyup)="onDetailsChange()" [(ngModel)]="practice.comments" placeholder="comments"/></td>
				</tr>
		</table>
		<button type="button" (click)="processForm()">{{isUpdate ? 'Update' : 'Save'}}</button>
		<button type="button" (click)="onCancel()">Cancel</button>
		<div *ngIf="processStatusMsg">
			<p><span [class.error]="processError">  {{processStatusMsg}} </span></p>
		</div>
			
	</div>
	`
	})
	
export class PracticeEdit implements OnChanges{
	
	@Input()
	practice: Practice;
	
	@Input()
	patientId: string;
	
	@Input()
	tooth: number;
	
	@Input()
	face: string;
	
	@Input()
	isUpdate: boolean;
	
	processError: boolean = false;
	processStatusMsg : string = "";
	updateStatus: number = 0;
	errorMessage: string = "";
	
	constructor(private patientService : PatientService) {}
		
	ngOnChanges(changes : SimpleChanges) {
		console.log("PracticeEdit ngOnChanges called");
		for(let propName in changes)
		{
			if(propName === "practice" && changes[propName]) {
				this.practice = changes[propName].currentValue;
				console.log("Cambio en property practice");
			}
		}
	}
	
	processForm() {
		this.processStatusMsg = "";
		if(this.isUpdate)
		{
			console.log("Updating practice");
			
			if(this.practice){
				this.patientService.updateFacePractice(this.practice, this.patientId, this.tooth, this.face).subscribe(err => this.processError = !err);
			}		
		}
		else
		{
			console.log("Adding practice");
		
			if(this.practice){
				this.patientService.addFacePractice(this.practice, this.patientId, this.tooth, this.face).subscribe(err => this.processError = !err);
				this.practice = null;
			}
		}
		
		//TODO This message is not shown
		if(this.processError)
		{
			this.processStatusMsg = "An error occurred, form processing failed";
		}
		else
		{
			this.processStatusMsg = "Form data processed successfuly";
		}
	}
	
	deletePractice() {
		console.log("Deleting practice");
		this.processStatusMsg = "";
		if(this.practice){
			this.patientService.deleteFacePractice(this.practice.id, this.patientId, this.tooth, this.face).subscribe(err => this.processError = !err);
		}
	}
	
	addPractice() {
		this.processStatusMsg = "";
		this.practice = new Practice();
		this.isUpdate = false;
	}
	
	onCancel() {
		this.processStatusMsg = "";
		this.practice = null;
	}
	
	onDetailsChange(){
		this.processStatusMsg = "";
	}
	
}