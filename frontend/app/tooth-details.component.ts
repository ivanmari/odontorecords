import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PatientService } from './patient.service'
	
import { Practice } from './practice'
import { PracticeEdit } from './practice-edit.component'


@Component({
	selector:'tooth-details',
	template: `
	<div *ngIf="patientId">
	<h1> T {{ toothId }} </h1>

			<div *ngIf="toothPractices">

				<div *ngFor= "let face of faces">
					{{face}}
					<button (click)="addPractice(face)">Add practice</button>
					<table class="hoverTable" border="1" width="100%">

					<thead>
					<tr>
							<th>Code</th>
							<th>Delivery Date</th>
					</tr>
					</thead>
					
					<tbody>
					
					<tr *ngFor= "let practice of toothPractices[face]"
							[class.selected]="practice === selectedPractice"
							(click)="onSelect(practice, face)">
						<td>
						<span class="badge">{{practice.code}}</span>
						</td>

						<td>
						{{practice.deliveryDate}}
						</td>
						
						<td>
						<button (click)="editPractice(practice, face)">Edit</button>
						</td>
						
						<td>
						<button (click)="deletePractice(practice, face)">Delete</button>
						</td>
						
					</tr>
					</tbody>
					</table>
				</div>
			</div>	

			<practice-edit [practice]="selectedPractice" [patientId]=patientId [tooth]="toothId" [face]="selectedFace" [isUpdate]="isUpdate"> </practice-edit>
	</div>

	`,
styles: [`
  	.hoverTable{
		width:100%; 
		border-collapse:collapse; 
	}
	.hoverTable td{ 
		padding:7px; border:#4e95f4 1px solid;
	}
	/* Define the default color for all the table rows */
	.hoverTable tr{
		background: #b8d1f3;
	}
	/* Define the hover highlight color for the table row */
    .hoverTable tr:hover {
          background-color: #ffff99;
    }
`],
  providers: [PatientService]
	})
	
export class ToothDetails implements OnChanges{
	
	//Genera tabla de practicas. Columna ToothFace / Fecha / Codigo
	@Input()
	toothId: number;

	@Input()
	patientId: string;
	
	//TODO Cargar mapa de cara a vector de practicas
	faces = ["Mesial", "Distal", "Lingual", "Vestibular", "Oclusal"];
	
	toothPractices: { [face: string]: Practice[] };

	isUpdate: boolean = true;
	selectedPractice: Practice;
	selectedFace: string;
	
	constructor(private patientService : PatientService) {}
	
	getPractices() {
		this.patientService.getToothPractices(this.patientId, this.toothId)
						   .subscribe(p => this.toothPractices = p);
	}
	
	ngOnChanges(changes : SimpleChanges) {
		console.log("Seleccionado nuevo diente");
		for(let propName in changes)
		{
			if(propName === "toothId" && changes[propName]) {
				let toothId = changes[propName].currentValue;
				
				if(toothId)
				{
					console.log("Diente nro:" + toothId);
					this.getPractices();
					this.isUpdate = true;
				}
			}
			
			if(propName === "patientId" && changes[propName]) {
				this.toothPractices = null;	
			}
		}
	}
	
	onSelect(practice: Practice, face: string){
		this.selectedPractice = practice;
		this.selectedFace = face;
		console.log("Practica seleccionada");
		console.log(this.selectedPractice);
	}
	
	editPractice(practice: Practice, face: string) {
		this.selectedPractice = practice;
		this.selectedFace = face;
		this.isUpdate = true;
		console.log("Edit Practice");
		console.log(this.selectedPractice);
	}
	
	
	
	addPractice(face: string) {
		this.selectedFace = face
		this.selectedPractice = new Practice();
		this.isUpdate = false;
		console.log("Edit Practice");
		console.log(this.selectedPractice);
	}
}