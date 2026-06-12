import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Patient } from './patient'
import { PatientService } from './patient.service'
import { PatientBasicInfo } from './patientbasic'
// removed deprecated @angular/http import
import { Mouth } from './mouth.component'

@Component({
	selector:'test-anidado',
	template: `
	
	<h1>Test Anidado {{numero}}</h1>
		<div *ngIf="patient" > Valor del patient = {{patient.id}} </div>
		`
	})
	
export class TestAnidado implements OnChanges{
	@Input()
	patient: PatientBasicInfo;
	
	@Input()
	numero: number = 0;
	
		ngOnInit(){
			console.log("Test Anidado OnInit");
		}
		
		ngOnChanges(changes : SimpleChanges) {
			console.log("Test Anidado ngOnChanges called");
			for(let propName in changes)
			{
			   console.log(propName);
			}
		}
		
		constructor(){}
}
